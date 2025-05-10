package com.example.reviewService.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Component
public class ReviewProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;



    public void sendReviewToWorker(String workerId, double averageRating) {
        Map<String, Object> message = new HashMap<>();
        message.put("workerId", workerId);
        message.put("averageRating", averageRating);

        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY, message);
        System.out.println("✅ Sent average rating to WorkerService: " + workerId + " = " + averageRating);
    }

}
