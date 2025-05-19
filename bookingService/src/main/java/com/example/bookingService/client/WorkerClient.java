package com.example.bookingService.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "worker-service", url = "http://worker-service:8082/workers")
public interface WorkerClient {

    @PutMapping("/add-timeslot")
    String addTimeSlot(@RequestHeader("X-Worker-Id") String workerId, @RequestParam("hour") int hour);

    @PutMapping("/remove-timeslot")
    String removeTimeSlot(@RequestHeader("X-Worker-Id") String workerId, @RequestParam("hour") int hour);
}