package com.jann.csv_data_hub.service;

import com.jann.csv_data_hub.messaging.RabbitConfig;
import com.jann.csv_data_hub.repository.DataQueryRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class DataQueryService {

    private final DataQueryRepository dataQueryRepository;

    public DataQueryService(DataQueryRepository dataQueryRepository) {
        this.dataQueryRepository = dataQueryRepository;
    }

    @RabbitListener(queues = RabbitConfig.TABLE_QUERY_QUEUE)
    public void query(String queryPayload) {

        System.out.println("Executing query...");

        //dataRepository.executeQuery(queryPayload);
    }
}