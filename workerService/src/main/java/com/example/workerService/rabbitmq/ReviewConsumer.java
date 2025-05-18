package com.example.workerService.rabbitmq;

import com.example.workerService.model.Worker;
import com.example.workerService.repository.WorkerRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.Optional;

@Component
public class ReviewConsumer {
    @Autowired
    private WorkerRepository workerRepository;

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void receiveMessage(Map<String, Object> message) {
        System.out.println("ana henaaaaaa");
        String workerId = (String) message.get("workerId");
        double averageRating = (double) message.get("averageRating");

        // Update the worker's average rating here
        System.out.println("📥 Received average rating for worker " + workerId + ": " + averageRating);

        Optional<Worker> optionalWorker = workerRepository.findById(workerId);
        if (optionalWorker.isPresent()) {
            Worker worker = optionalWorker.get();
            worker.setAverageRating(averageRating);
            workerRepository.save(worker);
            System.out.println("📦 Worker " + worker.getName() + " updated with new rating: " + averageRating);
        } else {
            System.out.println("⚠️ Worker not found for ID: " + workerId);
        }
    }
}
