package com.jann.csv_data_hub.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class DataIngestionService {

    @RabbitListener(queues = "csv.queue")
    public void process(String filePath) {

        System.out.println("Processing CSV: " + filePath);

        // aqui entra:
        // - leitura do CSV
        // - parsing
        // - persistência
        // - envio para outras filas se necessário
    }
}