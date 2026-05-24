package com.jann.csv_data_hub.exception.handler;

import com.jann.csv_data_hub.exception.error.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IOException.class)
    public ResponseEntity<String> handleIOException(IOException ex) {
        return ResponseEntity.status(500)
                .body("File processing error: " + ex.getMessage());
    }

    @ExceptionHandler(TableValidationException.class)
    public ResponseEntity<String> handleTableValidation(TableValidationException ex) {
        return ResponseEntity.badRequest()
                .body("Table validation error: " + ex.getMessage());
    }

    @ExceptionHandler(CsvValidationException.class)
    public ResponseEntity<String> handleCsvValidation(CsvValidationException ex) {
        return ResponseEntity.badRequest()
                .body("CSV validation error: " + ex.getMessage());
    }

    @ExceptionHandler(SqlTypeMappingException.class)
    public ResponseEntity<String> handleSqlType(SqlTypeMappingException ex) {
        return ResponseEntity.badRequest()
                .body("SQL type mapping error: " + ex.getMessage());
    }

    @ExceptionHandler(DataIngestionException.class)
    public ResponseEntity<String> handleDataIngestion(DataIngestionException ex) {
        return ResponseEntity.status(500)
                .body("Data ingestion error: " + ex.getMessage());
    }

    @ExceptionHandler(FileStorageException.class)
    public ResponseEntity<String> handleFileStorage(FileStorageException ex) {
        return ResponseEntity.status(500)
                .body("File Storage error: " + ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneric(Exception ex) {
        return ResponseEntity.status(500)
                .body("Unexpected error: " + ex.getMessage());
    }
}