package com.example.reviewService.rabbitmq;

import com.example.reviewService.ReviewRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReviewConsumer {

   @Autowired
    private ReviewRepository reviewRepository;

    @RabbitListener(queues = RabbitMQConfig.USER_DELETED_QUEUE)
    public void handleUserDeletedEvent(String userId) {
        System.out.println("Received User Deleted Event for userId: " + userId);

        // Soft delete or archive the reviews for this user
        reviewRepository.deleteByUserId(userId);

        System.out.println("🗑Archived reviews for userId: " + userId);
    }

}
