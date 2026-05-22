package com.jann.csv_data_hub.message.config.queue;

import com.jann.csv_data_hub.message.config.exchange.RabbitExchangeConfig;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;

public class RabbitQueueFactory {

    private static final String DLQ_EXCHANGE = RabbitExchangeConfig.EXCHANGE;

    public static Queue createQueue(String name) {
        return QueueBuilder.durable(name)
                .withArgument("x-dead-letter-exchange", DLQ_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", name + ".dlq")
                .build();
    }

    public static Queue createDLQ(String name) {
        return QueueBuilder.durable(name + ".dlq").build();
    }
}