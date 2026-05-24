package com.jann.csv_data_hub.message.consumer;

import com.jann.csv_data_hub.message.config.routing_keys.RabbitRoutingKeys;
import com.jann.csv_data_hub.message.tracker.domain.RequestStatus;
import com.jann.csv_data_hub.message.tracker.dto.RequestTrackerMessage;
import com.jann.csv_data_hub.message.tracker.service.RequestTrackerService;
import com.jann.csv_data_hub.model.file_storage.FileStorageInfo;
import com.jann.csv_data_hub.model.table.TableInfo;
import com.jann.csv_data_hub.service.DataIngestionService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class IngestionConsumer {

    private final DataIngestionService service;
    private final RequestTrackerService tracker;

    public IngestionConsumer(DataIngestionService service, RequestTrackerService tracker) {
        this.service = service;
        this.tracker = tracker;
    }

    @RabbitListener(queues = RabbitRoutingKeys.CSV_INGESTION)
    public void process(RequestTrackerMessage<FileStorageInfo> message) {
        tracker.executeStep(message.getRequestId(), RequestStatus.CONSUMED);
        service.process(message);
    }
}