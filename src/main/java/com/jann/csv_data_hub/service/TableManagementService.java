package com.jann.csv_data_hub.service;

import com.jann.csv_data_hub.messaging.RabbitConfig;
//import com.jann.csv_data_hub.repository.TableRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class TableManagementService {

//    private final TableRepository tableRepository;
//
//    public TableSchemaService(TableRepository tableRepository) {
//        this.tableRepository = tableRepository;
//    }

    @RabbitListener(queues = RabbitConfig.TABLE_CREATE_QUEUE)
    public void createTable(String schemaJson) {
        //Criar model

        System.out.println("Creating table...");

        // TODO: parse JSON (tableName + columns)

        //tableRepository.createTable(schemaJson);
    }

    @RabbitListener(queues = RabbitConfig.TABLE_DELETE_QUEUE)
    public void deleteTable(String tableName) {

        System.out.println("Deleting table: " + tableName);

        //tableRepository.dropTable(tableName);
    }
}