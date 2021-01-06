package dev.bolans.linefinderservice.infrastructure;

import lombok.Getter;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class LineFinderProperties {
    private String getAStarLibrariesScript = "a_star_import_libraries.R";
    private String getFlorLibrariesScript = "flor_import_libraries.R";

}
