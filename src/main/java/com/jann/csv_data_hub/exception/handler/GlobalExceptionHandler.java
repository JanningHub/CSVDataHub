package com.jann.csv_data_hub.exception.handler;

import com.jann.csv_data_hub.exception.error.TableValidationException;
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

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneric(Exception ex) {
        return ResponseEntity.status(500)
                .body("Unexpected error: " + ex.getMessage());
    }
}