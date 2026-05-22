package com.jann.csv_data_hub.message.tracker.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.Instant;

@Entity
public class RequestTracker {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String requestId;

    private String queueName;

    private RequestStatus status;

    private Instant updatedAt;

    public RequestTracker() {}

    public RequestTracker(String queueName, RequestStatus status, Instant updatedAt) {
        this.queueName = queueName;
        this.status = status;
        this.updatedAt = updatedAt;
    }

    public String getRequestId() { return requestId; }

    public String getQueueName() { return queueName; }

    public RequestStatus getStatus() { return status; }

    public Instant getUpdatedAt() { return updatedAt; }

    public void setStatus(RequestStatus status) { this.status = status; }

    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}