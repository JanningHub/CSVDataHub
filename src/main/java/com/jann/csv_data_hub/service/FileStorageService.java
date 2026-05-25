package com.jann.csv_data_hub.service;

import com.jann.csv_data_hub.exception.error.FileStorageException;
import com.jann.csv_data_hub.model.file_storage.FileStorageInfo;
import com.jann.csv_data_hub.repository.FileStorageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileStorageService {

    private final TableManagementService tableManagementService;
    private final FileStorageRepository fileStorageRepository;

    public FileStorageService(TableManagementService tableManagementService,
                              FileStorageRepository fileStorageRepository) {
        this.tableManagementService = tableManagementService;
        this.fileStorageRepository = fileStorageRepository;
    }

    @Value("${file.upload-dir}")
    private String uploadDir;

    public FileStorageInfo save(MultipartFile file, String tableName) throws IOException {
        tableManagementService.tableExists(tableName);

        Files.createDirectories(Paths.get(uploadDir));

        String originalName = file.getOriginalFilename();
        checkFileType(originalName, file.getContentType());

        String fileName = System.currentTimeMillis() + "_" + originalName;

        Path filePath = Paths.get(uploadDir, fileName);
        file.transferTo(filePath);

        return fileStorageRepository.save(new FileStorageInfo(filePath.toString(), tableName));
    }

    private void checkFileType(String originalName, String contentType) {
        if (originalName == null || !originalName.toLowerCase().endsWith(".csv")) {
            throw new FileStorageException("Only CSV files are allowed");
        }

        if (contentType != null && !contentType.equals("text/csv") && !contentType.equals("application/vnd.ms-excel")) {
            throw new FileStorageException("Invalid file type: " + contentType);
        }
    }

    public void deleteFile(Path path) {
        try {
            if (!Files.exists(path)) {
                throw new FileStorageException("File does not exist: " + path.getFileName());
            }

            Files.delete(path);
        } catch (IOException e) {
            throw new FileStorageException("Error deleting file: " + path.getFileName(), e);
        }
    }
}