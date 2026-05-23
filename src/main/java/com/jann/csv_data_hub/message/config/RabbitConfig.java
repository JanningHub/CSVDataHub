package com.jann.csv_data_hub.message.config;

import com.jann.csv_data_hub.message.config.queue.RabbitQueueFactory;
import com.jann.csv_data_hub.message.config.routing_keys.RabbitRoutingKeys;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public Queue tableCreateQueue() {
        return RabbitQueueFactory.createQueue(RabbitRoutingKeys.TABLE_CREATE);
    }

    @Bean
    public Queue tableDeleteQueue() {
        return RabbitQueueFactory.createQueue(RabbitRoutingKeys.TABLE_DELETE);
    }

    @Bean
    public Queue tableQueryQueue() {
        return RabbitQueueFactory.createQueue(RabbitRoutingKeys.TABLE_QUERY);
    }

    @Bean
    public Queue cvsIngestionQueue() {
        return RabbitQueueFactory.createQueue(RabbitRoutingKeys.CSV_INGESTION);
    }

    @Bean
    public Queue tableCreateDLQ() {
        return RabbitQueueFactory.createDLQ(RabbitRoutingKeys.TABLE_CREATE);
    }

    @Bean
    public Queue tableDeleteDLQ() {
        return RabbitQueueFactory.createDLQ(RabbitRoutingKeys.TABLE_DELETE);
    }

    @Bean
    public Queue tableQueryDLQ() {
        return RabbitQueueFactory.createDLQ(RabbitRoutingKeys.TABLE_QUERY);
    }

    @Bean
    public Queue csvIngestionDLQ() {
        return RabbitQueueFactory.createDLQ(RabbitRoutingKeys.CSV_INGESTION);
    }

    @Bean
    public Binding bindCreate(TopicExchange exchange, Queue tableCreateQueue) {
        return BindingBuilder.bind(tableCreateQueue)
                .to(exchange)
                .with(RabbitRoutingKeys.TABLE_CREATE);
    }

    @Bean
    public Binding bindDelete(TopicExchange exchange, Queue tableDeleteQueue) {
        return BindingBuilder.bind(tableDeleteQueue)
                .to(exchange)
                .with(RabbitRoutingKeys.TABLE_DELETE);
    }

    @Bean
    public Binding bindQuery(TopicExchange exchange, Queue tableQueryQueue) {
        return BindingBuilder.bind(tableQueryQueue)
                .to(exchange)
                .with(RabbitRoutingKeys.TABLE_QUERY);
    }

    @Bean
    public Binding bindIngestion(TopicExchange exchange, Queue csvIngestionQueue) {
        return BindingBuilder.bind(csvIngestionQueue)
                .to(exchange)
                .with(RabbitRoutingKeys.CSV_INGESTION);
    }

    @Bean
    public Binding bindCreateDLQ(TopicExchange exchange, Queue tableCreateDLQ) {
        return BindingBuilder.bind(tableCreateDLQ)
                .to(exchange)
                .with(RabbitRoutingKeys.dlq(RabbitRoutingKeys.TABLE_CREATE));
    }

    @Bean
    public Binding bindDeleteDLQ(TopicExchange exchange, Queue tableDeleteDLQ) {
        return BindingBuilder.bind(tableDeleteDLQ)
                .to(exchange)
                .with(RabbitRoutingKeys.dlq(RabbitRoutingKeys.TABLE_DELETE));
    }

    @Bean
    public Binding bindQueryDLQ(TopicExchange exchange, Queue tableQueryDLQ) {
        return BindingBuilder.bind(tableQueryDLQ)
                .to(exchange)
                .with(RabbitRoutingKeys.dlq(RabbitRoutingKeys.TABLE_QUERY));
    }

    @Bean
    public Binding bindIngestionDLQ(TopicExchange exchange, Queue csvIngestionDLQ) {
        return BindingBuilder.bind(csvIngestionDLQ)
                .to(exchange)
                .with(RabbitRoutingKeys.dlq(RabbitRoutingKeys.CSV_INGESTION));
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter converter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(converter);
        return template;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            Jackson2JsonMessageConverter converter) {
        SimpleRabbitListenerContainerFactory factory =
                new SimpleRabbitListenerContainerFactory();

        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(converter);

        return factory;
    }
}