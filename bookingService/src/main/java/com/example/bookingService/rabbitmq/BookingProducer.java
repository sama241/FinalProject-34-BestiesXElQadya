package com.example.bookingService.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class BookingProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendBookingStatusToWorker(String workerId, String timeSlot, String status) {
        Map<String, Object> message = new HashMap<>();
        message.put("workerId", workerId);
        message.put("timeSlot", timeSlot);
        message.put("status", status); // "confirmed" or "canceled"

        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY, message);
        System.out.println("✅ Sent booking status to WorkerService: " + workerId + " | " + timeSlot + " | " + status);
    }
}
