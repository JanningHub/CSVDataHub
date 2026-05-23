package com.jann.csv_data_hub.message.producer;

import com.jann.csv_data_hub.message.config.exchange.RabbitExchangeConfig;
import com.jann.csv_data_hub.message.config.routing_keys.RabbitRoutingKeys;
import com.jann.csv_data_hub.message.tracker.domain.RequestStatus;
import com.jann.csv_data_hub.message.tracker.service.RequestTrackerService;
import com.jann.csv_data_hub.message.tracker.dto.RequestTrackerMessage;
import com.jann.csv_data_hub.model.table.TableInfo;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class TableProducer {

    private final RabbitTemplate rabbitTemplate;
    private final RequestTrackerService tracker;

    public TableProducer(RabbitTemplate rabbitTemplate, RequestTrackerService tracker) {
        this.rabbitTemplate = rabbitTemplate;
        this.tracker = tracker;
    }

    public String create(TableInfo tableInfo) {
        RequestTrackerMessage<TableInfo> requestTrackerMessage = new RequestTrackerMessage<>(tracker
                .createRequestTracker(RabbitRoutingKeys.TABLE_CREATE, RequestStatus.PUBLISHED)
                .getRequestId(), tableInfo);

        rabbitTemplate.convertAndSend(
                RabbitExchangeConfig.EXCHANGE,
                RabbitRoutingKeys.TABLE_CREATE,
                requestTrackerMessage
        );

        return requestTrackerMessage.getRequestId();
    }

    public String delete(String tableName) {
        RequestTrackerMessage<String> requestTrackerMessage = new RequestTrackerMessage<>(tracker
                .createRequestTracker(RabbitRoutingKeys.TABLE_DELETE, RequestStatus.PUBLISHED)
                .getRequestId(), tableName);

        rabbitTemplate.convertAndSend(
                RabbitExchangeConfig.EXCHANGE,
                RabbitRoutingKeys.TABLE_DELETE,
                requestTrackerMessage
        );

        return requestTrackerMessage.getRequestId();
    }
}