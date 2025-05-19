package com.example.workerService.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BookingQueueConfig {

    public static final String QUEUE = "booking_to_worker_queue";
    public static final String EXCHANGE = "booking_exchange";
    public static final String ROUTING_KEY = "booking.worker";

    @Bean
    public Queue bookingQueue() {
        return new Queue(QUEUE, true);
    }

    @Bean
    public TopicExchange bookingExchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Binding bookingBinding(Queue bookingQueue, TopicExchange bookingExchange) {
        return BindingBuilder
                .bind(bookingQueue)
                .to(bookingExchange)
                .with(ROUTING_KEY);
    }
}
