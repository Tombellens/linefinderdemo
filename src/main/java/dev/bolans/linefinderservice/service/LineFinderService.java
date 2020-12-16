package dev.bolans.linefinderservice.service;

import com.github.rcaller.rstuff.*;
import com.github.rcaller.util.Globals;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LineFinderService {

    public int getLines() throws IOException, URISyntaxException {
        String scriptContent = getScriptContent();
        RCode rCode = RCode.create();
        rCode.addRCode(scriptContent);
        RCaller caller = RCaller.create(rCode, getRCallerOptions());
        caller.runAndReturnResult("lines");
        return caller.getParser().getAsIntArray("lines")[0];

    }

    String getScriptContent() throws IOException, URISyntaxException {
        URI scriptUri = LineFinderService.class.getClassLoader().getResource("line_finder_startup.R").toURI();
        Path inputScript = Paths.get(scriptUri);

        return Files.lines(inputScript).collect(Collectors.joining("\n"));
    }

    RCallerOptions getRCallerOptions(){
       String rScriptExecutable = "/usr/bin/Rscript";
       String rExecutable = "/usr/bin/R";
       return RCallerOptions.create(rScriptExecutable, rExecutable, FailurePolicy.RETRY_5, Long.MAX_VALUE, 100, RProcessStartUpOptions.create());
    }
}
