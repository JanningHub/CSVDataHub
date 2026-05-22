package com.jann.csv_data_hub.message.tracker.dto;
import com.jann.csv_data_hub.message.tracker.domain.RequestStatus;

import java.time.Instant;

public record RequestTrackerResponse(

        String requestId,

        String queueName,

        RequestStatus status,

        Instant updatedAt
) {
}