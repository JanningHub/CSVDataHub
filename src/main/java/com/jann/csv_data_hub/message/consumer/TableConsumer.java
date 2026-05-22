package com.jann.csv_data_hub.message.consumer;

import com.jann.csv_data_hub.message.config.routing_keys.RabbitRoutingKeys;
import com.jann.csv_data_hub.message.tracker.domain.RequestStatus;
import com.jann.csv_data_hub.message.tracker.dto.RequestTrackerMessage;
import com.jann.csv_data_hub.message.tracker.service.RequestTrackerService;
import com.jann.csv_data_hub.model.TableInfo;
import com.jann.csv_data_hub.service.TableManagementService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class TableConsumer {

    private final TableManagementService service;
    private final RequestTrackerService tracker;

    public TableConsumer(TableManagementService service, RequestTrackerService tracker) {
        this.service = service;
        this.tracker = tracker;
    }

    @RabbitListener(queues = RabbitRoutingKeys.TABLE_CREATE)
    public void create(RequestTrackerMessage<TableInfo> message) {
        tracker.executeStep(message.getRequestId(), RequestStatus.CONSUMED);
        service.createTable(message);
    }

    @RabbitListener(queues = RabbitRoutingKeys.TABLE_DELETE)
    public void delete(RequestTrackerMessage<String> message) {
        tracker.executeStep(message.getRequestId(), RequestStatus.CONSUMED);
        service.deleteTable(message);
    }
}