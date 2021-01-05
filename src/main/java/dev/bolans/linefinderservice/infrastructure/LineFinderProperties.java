package dev.bolans.linefinderservice.infrastructure;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class LineFinderProperties {
    @Value("${application.r.scripts.astar.libraries")
    private String getAStarLibrariesScript;
    @Value("${application.r.scripts.flor.libraries")
    private String getFlorLibrariesScript;

}
