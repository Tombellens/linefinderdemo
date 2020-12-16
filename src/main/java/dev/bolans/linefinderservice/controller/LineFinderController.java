package dev.bolans.linefinderservice.controller;

import dev.bolans.linefinderservice.service.LineFinderService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;

@RestController
@RequestMapping(path="line-finder")
@RequiredArgsConstructor
@Slf4j
public class LineFinderController {
    @NonNull LineFinderService lineFinderService;

    @PostMapping(path="/get-lines")
    @ResponseStatus(HttpStatus.CREATED)
    public int getLines() throws IOException, URISyntaxException {
        return lineFinderService.getLines();
    }

}
