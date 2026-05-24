package com.jann.csv_data_hub.exception.error;

public class SqlTypeMappingException extends RuntimeException {

    public SqlTypeMappingException(String message) {
        super(message);
    }

    public SqlTypeMappingException(String message, Throwable cause) {
        super(message, cause);
    }
}