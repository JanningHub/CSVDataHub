package com.jann.csv_data_hub.messaging;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String TABLE_CREATE_QUEUE = "csv.table.create.queue";
    public static final String TABLE_INSERT_QUEUE = "csv.table.insert.queue";
    public static final String TABLE_DELETE_QUEUE = "csv.table.delete.queue";
    public static final String TABLE_QUERY_QUEUE = "csv.table.query.queue";

    @Bean
    public Queue tableCreateQueue() {
        return new Queue(TABLE_CREATE_QUEUE, true);
    }

    @Bean
    public Queue tableInsertQueue() {
        return new Queue(TABLE_INSERT_QUEUE, true);
    }

    @Bean
    public Queue tableDeleteQueue() {
        return new Queue(TABLE_DELETE_QUEUE, true);
    }

    @Bean
    public Queue tableQueryQueue() {
        return new Queue(TABLE_QUERY_QUEUE, true);
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory) {

        SimpleRabbitListenerContainerFactory factory =
                new SimpleRabbitListenerContainerFactory();

        factory.setConnectionFactory(connectionFactory);

        factory.setConcurrentConsumers(2);
        factory.setMaxConcurrentConsumers(10);

        return factory;
    }
}