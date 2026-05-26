package com.jann.csv_data_hub.exception.error;

public class DataQueryAnalyticsException extends RuntimeException {

    public DataQueryAnalyticsException(String message) {
        super(message);
    }

    public DataQueryAnalyticsException(String message, Throwable cause) {
        super(message, cause);
    }
}
