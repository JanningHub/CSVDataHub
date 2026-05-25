package com.jann.csv_data_hub.controller;

import com.jann.csv_data_hub.message.producer.IngestionProducer;
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

@Validated
@RestController
@RequestMapping("/data-ingestion")
public class DataIngestionController {

    private final FileStorageService fileStorageService;
    private final IngestionProducer producer;

    public DataIngestionController(FileStorageService fileStorageService, IngestionProducer producer) {
        this.fileStorageService = fileStorageService;
        this.producer = producer;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadCsv(@RequestParam("file") MultipartFile file,
                                            @RequestParam("tableName") @NotBlank String tableName) throws IOException {
        FileStorageInfo fileStorageInfo = fileStorageService.save(file, tableName);

        String requestId = producer.process(fileStorageInfo);

        String response = String.format(
                "File received and queued for processing. Request ID: %s",
                requestId
        );

        return ResponseEntity.accepted().body(response);
    }
}
