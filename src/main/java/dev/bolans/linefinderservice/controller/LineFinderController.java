package dev.bolans.linefinderservice.controller;

import dev.bolans.linefinderservice.domain.BinarizationType;
import dev.bolans.linefinderservice.domain.FlorBinarizationType;
import dev.bolans.linefinderservice.service.dto.AStarLineFinderDto;
import dev.bolans.linefinderservice.service.LineFinderService;
import dev.bolans.linefinderservice.service.dto.FlorLineFinderDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;

@RestController
@RequestMapping(path="line-finder")
@RequiredArgsConstructor
@Slf4j
public class LineFinderController {
    @NonNull LineFinderService lineFinderService;

    @PostMapping(path="/a-star")
    @ResponseStatus(HttpStatus.OK)
    public AStarLineFinderDto getLines(@RequestParam("file") MultipartFile multipartFile,
                                       @RequestParam(value = "binarizationType", defaultValue = "SAUVOLA") BinarizationType binarizationType,
                                       @RequestParam(value = "morph", defaultValue = "true") boolean morph,
                                       @RequestParam(value = "step", defaultValue = "2") int step,
                                       @RequestParam(value ="mFactor", defaultValue = "5") int mFactor,
                                       @RequestParam(value = "trace", defaultValue = "true") boolean trace
                        ) throws IOException, URISyntaxException {
        return lineFinderService.aStarPathFinding(multipartFile, binarizationType, morph, step, mFactor, trace);
    }

    @PostMapping(path="/flor")
    @ResponseStatus(HttpStatus.OK)
    public FlorLineFinderDto getLines(@RequestParam("file") MultipartFile multipartFile,
                                      @RequestParam(value = "binarizationType", defaultValue = "SAUVOLA") BinarizationType binarizationType,
                                      @RequestParam(value = "light", defaultValue = "true") boolean light,
                                      @RequestParam(value = "florBinarizationType", defaultValue = "SAUVOLA")FlorBinarizationType florBinarizationType) throws IOException, URISyntaxException {
        return lineFinderService.florPathFinding(multipartFile, binarizationType, florBinarizationType, light);
    }
}
