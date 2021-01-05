package dev.bolans.linefinderservice;

import dev.bolans.linefinderservice.service.StorageService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@Slf4j
@RequiredArgsConstructor
public class LineFinderServiceApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder applicationBuilder){
        return applicationBuilder.sources(LineFinderServiceApplication.class);
    }
    public static void main(String[] args) {
        SpringApplication.run(LineFinderServiceApplication.class, args);
        log.info("Started LineFinder!");
    }

}
