package com.example.workerService.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name = "booking-service", url = "http://booking-service:8083/bookings")
public interface BookingClient {

    @GetMapping("/get/worker/{workerId}")
    List<Map<String, Object>> getBookingsByWorkerId(@PathVariable("workerId") String workerId);
}
