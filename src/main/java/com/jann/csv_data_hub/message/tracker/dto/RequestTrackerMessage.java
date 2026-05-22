package com.jann.csv_data_hub.message.tracker.dto;

import java.io.Serializable;

public class RequestTrackerMessage<T> implements Serializable {

    private String requestId;
    private T payload;

    public RequestTrackerMessage() {
    }

    public RequestTrackerMessage(String requestId, T payload) {
        this.requestId = requestId;
        this.payload = payload;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }
}
