package dev.bolans.linefinderservice.domain;

import lombok.Getter;

@Getter
public enum BinarizationType {
    OTSU("otsu"),
    SAUVOLA("sauvola"),
    WOLF("wolf"),
    TRSINGH("trsingh"),
    BATAINEH("bataineh"),
    WAN("wan");


    BinarizationType(String label){
        this.label = label;
    }
    String label;
}
