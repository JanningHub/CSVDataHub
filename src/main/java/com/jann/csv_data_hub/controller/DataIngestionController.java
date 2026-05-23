package com.jann.csv_data_hub.controller;

import com.jann.csv_data_hub.model.file_storage.FileStorageInfo;
import com.jann.csv_data_hub.service.FileStorageService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

@Validated
@RestController
@RequestMapping("/data-ingestion")
public class DataIngestionController {

    private final FileStorageService fileStorageService;

    public DataIngestionController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadCsv(@RequestParam("file") @NotBlank MultipartFile file,
                                            @RequestParam("tableName") @NotBlank String tableName) throws IOException {
        FileStorageInfo fileStorageInfo = fileStorageService.save(file, tableName);

        //csvMaestroService.sendToQueue(filePath);
        //Nao enviar para o maestro, acho que direto para a ingestao de dados

        return ResponseEntity.accepted()
                .body("File received and queued for processing");
    }
}
