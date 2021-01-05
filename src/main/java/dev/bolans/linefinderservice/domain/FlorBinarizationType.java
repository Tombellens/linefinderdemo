package dev.bolans.linefinderservice.domain;

import lombok.Getter;

@Getter
public enum FlorBinarizationType {
    NONE("none"),
    NIBLACK("niblack"),
    SAUVOLA("sauvola"),
    WOLF("wolf");

    FlorBinarizationType(String label){
        this.label = label;
    }
    String label;
}
