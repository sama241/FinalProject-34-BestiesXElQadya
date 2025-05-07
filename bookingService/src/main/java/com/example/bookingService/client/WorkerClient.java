package com.example.bookingService.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "worker-service", url = "http://localhost:8082/workers")
public interface WorkerClient {

    @PutMapping("/{workerId}/add-timeslot")
    String addTimeSlot(@PathVariable("workerId") String workerId, @RequestParam("hour") int hour);

    @PutMapping("/{workerId}/remove-timeslot")
    String removeTimeSlot(@PathVariable("workerId") String workerId, @RequestParam("hour") int hour);
}


