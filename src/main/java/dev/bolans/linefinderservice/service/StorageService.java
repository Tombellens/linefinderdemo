package dev.bolans.linefinderservice.service;

import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;

public interface StorageService {
    void init();

    void store(InputStream inputStream, String name);

    Resource load(String filename);

    void deleteAll();

    void deleteFile(String fileName) throws IOException;
}
