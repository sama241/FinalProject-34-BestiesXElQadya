package com.example.workerService.rabbitmq;

import com.example.workerService.service.WorkerService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class WorkerConsumer {

    private final WorkerService workerService;

    public WorkerConsumer(WorkerService workerService) {
        this.workerService = workerService;
    }

    @RabbitListener(queues = "review_to_worker_queue")
    public void handleReviewUpdate(Map<String, Object> message) {
        String workerId = (String) message.get("workerId");
        Double avgRating = (Double) message.get("averageRating");

        System.out.println("📩 Received review update for worker: " + workerId + ", avgRating: " + avgRating);

        workerService.updateAverageRating(workerId, avgRating);
    }
}
