package com.jann.csv_data_hub.exception.error;

public class TableValidationException extends RuntimeException {
    public TableValidationException(String message) {
        super(message);
    }
}