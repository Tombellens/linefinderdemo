package dev.bolans.linefinderservice.service;

import com.github.rcaller.rstuff.*;
import dev.bolans.linefinderservice.domain.BinarizationType;
import dev.bolans.linefinderservice.domain.FlorBinarizationType;
import dev.bolans.linefinderservice.infrastructure.LineFinderProperties;
import dev.bolans.linefinderservice.service.dto.AStarLineFinderDto;
import dev.bolans.linefinderservice.service.dto.FlorLineFinderDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
@RequiredArgsConstructor
public class LineFinderService {
    private Path root = null;
    private static final File TEMP_DIRECTORY = new File(System.getProperty("java.io.tmpdir"));

    @NonNull StorageService storageService;
    @NonNull LineFinderProperties lineFinderProperties;

    @PostConstruct
    public void init(){
        File newDirectory = new File(TEMP_DIRECTORY, "results");
        if (!newDirectory.exists()) newDirectory.mkdir();
        this.root = newDirectory.toPath();
    }

    public AStarLineFinderDto aStarPathFinding(MultipartFile multipartFile, BinarizationType binarizationType, Boolean morph, Integer step, Integer mfactor, Boolean trace) throws IOException, URISyntaxException {
        storageService.store(multipartFile.getInputStream(), multipartFile.getOriginalFilename());
        RCode rCode = RCode.create();
        setAStarRCode(rCode, storageService.load(multipartFile.getOriginalFilename()), binarizationType, morph, step, mfactor, trace);
        RCaller caller = RCaller.create(rCode, getRCallerOptions());
        caller.runAndReturnResult("lines");

        int lines =  caller.getParser().getAsIntArray("lines")[0];
        String fileName = multipartFile.getOriginalFilename();
        byte[] allBytes = getResultFileByteArray(fileName);
        storageService.deleteFile(fileName);

        return new AStarLineFinderDto(fileName, allBytes, lines);
    }
    public FlorLineFinderDto florPathFinding(MultipartFile multipartFile, BinarizationType binarizationType, FlorBinarizationType florBinarizationType, Boolean light) throws IOException, URISyntaxException {
        storageService.store(multipartFile.getInputStream(), multipartFile.getOriginalFilename());
        RCode rCode = RCode.create();
        setFlorRCode(rCode, storageService.load(multipartFile.getOriginalFilename()), binarizationType, florBinarizationType, light);
        RCaller caller = RCaller.create(rCode, getRCallerOptions());
        caller.runAndReturnResult("to_return");

        int lines =  caller.getParser().getAsIntArray("to_return")[0];
        int totalWords =  caller.getParser().getAsIntArray("to_return")[1];
        String fileName = multipartFile.getOriginalFilename();
        byte[] allBytes = getResultFileByteArray(fileName);
        List<byte[]> listOfWordByteArrays = IntStream.rangeClosed(1, lines)
                                                .mapToObj(i -> getResultFileByteArray(i+fileName))
                                                .collect(Collectors.toList());
        storageService.deleteFile(fileName);

        return new FlorLineFinderDto(fileName, allBytes,listOfWordByteArrays, lines, totalWords);
    }

    String getScriptContent(String scriptPath) throws IOException, URISyntaxException {
        URI scriptUri = LineFinderService.class.getClassLoader().getResource(scriptPath).toURI();
        Path inputScript = Paths.get(scriptUri);

        return Files.lines(inputScript).collect(Collectors.joining("\n"));
    }

    RCallerOptions getRCallerOptions(){
//      UNIX PATHS
        String rScriptExecutable = "/usr/bin/Rscript";
        String rExecutable = "/usr/bin/R";
//       WINDOWS-PATHS
//       String rScriptExecutable = "E:/_development/programs/R-4.0.3/bin/Rscript.exe";
//       String rExecutable = "E:/_development/programs/R-4.0.3/bin/R.exe";
       return RCallerOptions.create(rScriptExecutable, rExecutable, FailurePolicy.RETRY_5, Long.MAX_VALUE, 100, RProcessStartUpOptions.create());
    }

    private void setAStarRCode(RCode rcode, Resource resource, BinarizationType binarizationType, Boolean morph, Integer step, Integer mfactor, Boolean trace) throws IOException, URISyntaxException {
        rcode.addRCode(getScriptContent(lineFinderProperties.getGetAStarLibrariesScript()));
        rcode.addRCode("path <- \"" + resource.getURL().getPath().replace("/C:", "C:") + "\"");
        rcode.addRCode("img <- image_read(path)");
        rcode.addRCode("img <- image_binarization(img, type =\"" + binarizationType.getLabel() + "\")");
        rcode.addRCode("areas <- image_textlines_astar(img, morph = " + morph.toString().toUpperCase()
                        + " , step = " + step.toString() + ", mfactor = " + mfactor.toString() + " , trace = " + trace.toString().toUpperCase() + ")");
        rcode.addRCode("result <- areas$overview");
        rcode.addRCode("ocv_write(result, path=\"" + this.root.toString().replace('\\', '/') + '/' + resource.getFilename() + "\")");
        rcode.addRCode("lines <- areas$n");
    }

    private void setFlorRCode(RCode rcode, Resource resource, BinarizationType binarizationType, FlorBinarizationType florBinarizationType, Boolean light) throws IOException, URISyntaxException {
        rcode.addRCode(getScriptContent(lineFinderProperties.getGetFlorLibrariesScript()));
        rcode.addRCode("path <- \"" + resource.getURL().getPath().replace("/C:", "C:") + "\"");
        rcode.addRCode("img <- image_read(path)");
        rcode.addRCode("img <- image_read(path)");
        rcode.addRCode("img <- image_binarization(img, type =\"" + binarizationType.getLabel() + "\")");
        rcode.addRCode("areas <- image_textlines_flor(img, light = " + light.toString().toUpperCase() +
                ", type = \"" + florBinarizationType.getLabel() + "\")");
        rcode.addRCode("ocv_write(areas$overview, path=\"" + this.root.toString().replace('\\', '/') + '/' + resource.getFilename() + "\")");
        rcode.addRCode("lines <- areas$n");
        rcode.addRCode("total_words <- 0 ");
        rcode.addRCode("counter <- 0");
        rcode.addRCode("for (textline in areas$textlines){\n" +
                "  counter <- counter + 1\n" +
                "  textwords <- image_wordsegmentation(textline)\n" +
                "  total_words <- total_words + textwords$n\n" +
                "  ocv_write(textwords$overview, sprintf('" + this.root.toString().replace('\\', '/') +"/%d"+ resource.getFilename() + "', counter))\n" +
                "}");
        rcode.addRCode("to_return <- c(lines, total_words)");
    }

    private byte[] getResultFileByteArray(String fileName){
        try {
            return Files.readAllBytes(Paths.get("results/" + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
