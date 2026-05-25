package com.jann.csv_data_hub.message.tracker.service;

import com.jann.csv_data_hub.exception.error.RequestTrackerException;
import com.jann.csv_data_hub.message.tracker.domain.RequestStatus;
import com.jann.csv_data_hub.message.tracker.domain.RequestTracker;
import com.jann.csv_data_hub.message.tracker.repository.RequestTrackerRepository;
import com.jann.csv_data_hub.message.tracker.dto.RequestTrackerResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class RequestTrackerService {

    private final RequestTrackerRepository repository;

    public RequestTrackerService(RequestTrackerRepository repository) {
        this.repository = repository;
    }

    public RequestTracker createRequestTracker(String queueName, RequestStatus status) {
        return repository.save(new RequestTracker(queueName, status, Instant.now()));
    }

    public void executeStep(String requestId, RequestStatus newStatus) {
        RequestTracker tracker = repository.findById(requestId)
                .orElseThrow(() -> new RuntimeException(
                        "Tracker not found for requestId: " + requestId
                ));

        tracker.setStatus(newStatus);
        tracker.setUpdatedAt(Instant.now());

        repository.save(tracker);
    }

    public Page<RequestTrackerResponse> getRequestTrackers(Pageable pageable) {
        Page<RequestTracker> page = repository.findAll(pageable);

        if (page.isEmpty()) {
            throw new RequestTrackerException("No trackers found");
        }

        return page.map(requestTracker ->
                new RequestTrackerResponse(
                        requestTracker.getRequestId(),
                        requestTracker.getQueueName(),
                        requestTracker.getStatus(),
                        requestTracker.getUpdatedAt()
                )
        );
    }

    public RequestTrackerResponse getRequestTrackerById(String requestId) {
        RequestTracker requestTracker = repository.findById(requestId)
                .orElseThrow(() -> new RequestTrackerException(
                        "Tracker not found for requestId: " + requestId
                ));

        return new RequestTrackerResponse(requestTracker.getRequestId(), requestTracker.getQueueName(),
                requestTracker.getStatus(), requestTracker.getUpdatedAt());
    }
}
