package com.jann.csv_data_hub.service;

import com.jann.csv_data_hub.messaging.RabbitConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

@Service
public class CsvMaestroService {

    private final RabbitTemplate rabbitTemplate;

    public CsvMaestroService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void dispatchCreateTable(String tableSchemaJson) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.TABLE_CREATE_QUEUE,
                tableSchemaJson
        );
    }

    public void dispatchInsertData(Path filePath) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.TABLE_INSERT_QUEUE,
                filePath.toString()
        );
    }

    public void dispatchDeleteTable(String tableName) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.TABLE_DELETE_QUEUE,
                tableName
        );
    }

    public void dispatchQuery(String queryJson) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.TABLE_QUERY_QUEUE,
                queryJson
        );
    }
}