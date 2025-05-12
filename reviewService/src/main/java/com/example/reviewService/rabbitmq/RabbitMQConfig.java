package com.example.reviewService.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE = "review_to_worker_queue";
    public static final String EXCHANGE = "review_exchange";
    public static final String ROUTING_KEY = "review.worker";

    // New Queue for User Deleted Events
    public static final String USER_DELETED_QUEUE = "user_to_review_queue";
    public static final String USER_DELETED_EXCHANGE = "user_exchange";
    public static final String USER_DELETED_ROUTING_KEY = "user.review";

    @Bean
    public Queue queue() {
        return new Queue(QUEUE, true);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(ROUTING_KEY);
    }
    @Bean
    public Queue userDeletedQueue() {
        return new Queue(USER_DELETED_QUEUE, true);
    }

    @Bean
    public TopicExchange userDeletedExchange() {
        return new TopicExchange(USER_DELETED_EXCHANGE);
    }

    @Bean
    public Binding userDeletedBinding(Queue userDeletedQueue, TopicExchange userDeletedExchange) {
        return BindingBuilder.bind(userDeletedQueue).to(userDeletedExchange).with(USER_DELETED_ROUTING_KEY);
    }
}
