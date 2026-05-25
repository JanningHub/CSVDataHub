package com.jann.csv_data_hub.exception.error;

public class RequestTrackerException extends RuntimeException {

    public RequestTrackerException(String message) {
        super(message);
    }

    public RequestTrackerException(String message, Throwable cause) {
        super(message, cause);
    }
}
