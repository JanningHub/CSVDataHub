package com.jann.csv_data_hub.exception.error;

public class DataIngestionException extends RuntimeException {

    public DataIngestionException(String message) {
        super(message);
    }

    public DataIngestionException(String message, Throwable cause) {
        super(message, cause);
    }
}