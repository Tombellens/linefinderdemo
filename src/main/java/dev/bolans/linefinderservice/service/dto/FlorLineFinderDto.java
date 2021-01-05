package dev.bolans.linefinderservice.service.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class FlorLineFinderDto {
    String fileName;
    int totalLines;
    int totalWords;
    byte[] fullImageLines;
    List<byte[]> fullImageWords;

    public FlorLineFinderDto(String fileName, byte[] fullImage, List<byte[]> fullImageWords, int totalLines, int totalWords) {
        this.fileName = fileName;
        this.fullImageLines = fullImage;
        this.totalLines = totalLines;
        this.totalWords = totalWords;
        this.fullImageWords = fullImageWords;
    }


}
