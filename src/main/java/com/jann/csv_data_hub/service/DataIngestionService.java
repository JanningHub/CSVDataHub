package com.jann.csv_data_hub.service;

import com.jann.csv_data_hub.model.file_storage.FileStorageInfo;
import org.springframework.stereotype.Service;

@Service
public class DataIngestionService {

    public void process(FileStorageInfo fileStorageInfo) {

        System.out.println("Processing CSV: " + fileStorageInfo.getFilePath().getFileName().toString());

        // aqui entra:
        // - leitura do CSV
        // - parsing
        // - persistência
        // - envio para outras filas se necessário
    }
}