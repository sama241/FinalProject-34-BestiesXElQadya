package com.example.workerService.rabbitmq;

import com.example.workerService.model.Worker;
import com.example.workerService.repository.WorkerRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.Optional;

@Component
public class BookingConsumer {

    @Autowired
    private WorkerRepository workerRepository;

    @RabbitListener(queues = BookingQueueConfig.QUEUE)
    public void receiveBookingUpdate(Map<String, Object> message) {
        String workerId = (String) message.get("workerId");
        String timeSlot = (String) message.get("timeSlot"); // Expecting e.g. "14" or "10"
        String status = (String) message.get("status");     // "confirmed" or "canceled"

        int hour;
        try {
            hour = Integer.parseInt(timeSlot);
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid timeSlot: " + timeSlot);
            return;
        }

        Optional<Worker> optionalWorker = workerRepository.findById(workerId);
        if (optionalWorker.isPresent()) {
            Worker worker = optionalWorker.get();

            if (status.equalsIgnoreCase("confirmed")) {
                worker.removeAvailableHour(hour); // remove hour if booked
                System.out.println("📕 Hour " + hour + " removed for booking.");
            } else if (status.equalsIgnoreCase("canceled")) {
                worker.addAvailableHour(hour);   // re-add hour if booking was canceled
                System.out.println("📗 Hour " + hour + " restored after cancel.");

            }

            workerRepository.save(worker);
            System.out.println("✅ Worker " + worker.getName() + " availability updated.");
        } else {
            System.out.println("⚠️ Worker not found: " + workerId);
        }
    }
}
