package com.example.bookingService.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "worker-service", url = "http://worker-service:8082")
public interface WorkerClient {

    @PutMapping("/workers/{id}/AddTimeslots")
    String addTimeSlots(@PathVariable String id, @RequestBody int timeSlots);

    @PutMapping("/workers/{id}/RemoveTimeslots")
    String removeTimeSlots(@PathVariable String id, @RequestBody int timeSlot);
}

