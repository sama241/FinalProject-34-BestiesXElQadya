package com.example.userService.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Component
public class UserProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendDeleteToReview(String userId) {


        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY,userId);
        System.out.println("User deleted sent to review with ID :" + userId);
    }

}










