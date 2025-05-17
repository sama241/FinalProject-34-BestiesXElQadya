package com.example.reviewService.Client;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "worker-service", url = "http://worker-service:8082/workers") // The WorkerService URL (change the port if different)
public interface workerClient {

    // This endpoint will be used to update the worker's average rating
    @PutMapping("/{workerId}/average-rating")
     void updateAverageRating(@PathVariable String workerId, @RequestBody double rating);
}
