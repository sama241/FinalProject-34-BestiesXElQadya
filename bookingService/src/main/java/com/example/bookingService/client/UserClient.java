package com.example.bookingService.client;

import jakarta.servlet.http.HttpSession;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service", url = "http://localhost:8081/api/user/auth")
public interface UserClient {

    @GetMapping("/validate-session")
    ResponseEntity<String> validateSession(@RequestParam String sessionId);
}


