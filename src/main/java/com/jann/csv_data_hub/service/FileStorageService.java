package com.jann.csv_data_hub.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileStorageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    public Path save(MultipartFile file) throws IOException {
        Files.createDirectories(Paths.get(uploadDir));

        String originalName = file.getOriginalFilename();

        checkFileType(originalName, file.getContentType());

        String fileName = System.currentTimeMillis() + "_" + originalName;

        Path filePath = Paths.get(uploadDir, fileName);
        file.transferTo(filePath);

        return filePath;
    }

    private void checkFileType(String originalName, String contentType) {
        if (originalName == null || !originalName.toLowerCase().endsWith(".csv")) {
            throw new IllegalArgumentException("Only CSV files are allowed");
        }

        if (contentType != null && !contentType.equals("text/csv") && !contentType.equals("application/vnd.ms-excel")) {
            throw new IllegalArgumentException("Invalid file type: " + contentType);
        }
    }
}