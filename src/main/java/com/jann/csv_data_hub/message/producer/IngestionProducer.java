package com.jann.csv_data_hub.message.producer;

import com.jann.csv_data_hub.message.config.exchange.RabbitExchangeConfig;
import com.jann.csv_data_hub.message.config.routing_keys.RabbitRoutingKeys;
import com.jann.csv_data_hub.message.tracker.domain.RequestStatus;
import com.jann.csv_data_hub.message.tracker.service.RequestTrackerService;
import com.jann.csv_data_hub.message.tracker.dto.RequestTrackerMessage;
import com.jann.csv_data_hub.model.file_storage.FileStorageInfo;
import com.jann.csv_data_hub.model.table.TableInfo;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class IngestionProducer {

    private final RabbitTemplate rabbitTemplate;
    private final RequestTrackerService tracker;

    public IngestionProducer(RabbitTemplate rabbitTemplate, RequestTrackerService tracker) {
        this.rabbitTemplate = rabbitTemplate;
        this.tracker = tracker;
    }

    public String process(FileStorageInfo fileStorageInfo) {
        RequestTrackerMessage<FileStorageInfo> requestTrackerMessage = new RequestTrackerMessage<>(tracker
                .createRequestTracker(RabbitRoutingKeys.CSV_INGESTION, RequestStatus.PUBLISHED)
                .getRequestId(), fileStorageInfo);

        rabbitTemplate.convertAndSend(
                RabbitExchangeConfig.EXCHANGE,
                RabbitRoutingKeys.CSV_INGESTION,
                requestTrackerMessage
        );

        return requestTrackerMessage.getRequestId();
    }
}