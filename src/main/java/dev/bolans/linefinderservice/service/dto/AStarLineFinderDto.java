package dev.bolans.linefinderservice.service.dto;

import lombok.Getter;

@Getter
public class AStarLineFinderDto {
    String fileName;
    int totalLines;
    byte[] fullImage;

    public AStarLineFinderDto(String fileName, byte[] fullImage, int totalLines) {
        this.fileName = fileName;
        this.fullImage = fullImage;
        this.totalLines = totalLines;
    }
}
