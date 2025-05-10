package com.example.userService.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name = "booking-service", url = "http://booking-service:8083/bookings")
public interface BookingClient {

    @GetMapping("/user/{userId}")
    List<Map<String, Object>>  getBookingsByUserId(@PathVariable("userId") String userId);
}
