package com.example.reviewService.Observer;

import com.example.reviewService.Observer.ReviewObserver;
import com.example.reviewService.event.ReviewEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class WorkerServiceClient implements ReviewObserver {

    private final RestTemplate restTemplate;

    // Constructor injection (no @Autowired needed in Spring 4.3+)
    public WorkerServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void update(ReviewEvent event) {
        // Log the event data (Optional)
        System.out.println("Received review event: " + event);

        // Send the event data to the worker service
        restTemplate.postForObject(
                "http://worker-service/api/ratings",
                event,
                Void.class
        );
    }
}