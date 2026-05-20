package com.jann.csv_data_hub.controller;

import com.jann.csv_data_hub.service.CsvMaestroService;
import com.jann.csv_data_hub.service.FileStorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

@RestController
@RequestMapping("/data-ingestion")
public class DataIngestionController {

    private final FileStorageService fileStorageService;
    private final CsvMaestroService csvMaestroService;

    public DataIngestionController(FileStorageService fileStorageService,
                         CsvMaestroService csvMaestroService ) {
        this.fileStorageService = fileStorageService;
        this.csvMaestroService = csvMaestroService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadCsv(@RequestParam("file") MultipartFile file,
                                            @RequestParam("tableName") String tableName) throws IOException {
        //Deve verificar se a tabela existe, se nao fale para conferir o nome da tabela ou criar no /create-table

        Path filePath = fileStorageService.save(file);

        //csvMaestroService.sendToQueue(filePath);
        //Nao enviar para o maestro, acho que direto para a ingestao de dados

        return ResponseEntity.accepted()
                .body("File received and queued for processing");
    }
}
