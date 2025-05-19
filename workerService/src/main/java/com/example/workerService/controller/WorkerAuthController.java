package com.example.workerService.controller;


import com.example.workerService.model.Worker;
import com.example.workerService.repository.WorkerRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/worker/auth")
public class WorkerAuthController {

    @Autowired
    private WorkerRepository workerRepository;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(
            @RequestBody Map<String, String> loginRequest,
            HttpSession session) {

        String email = loginRequest.get("email");
        String password = loginRequest.get("password");

        Worker worker = workerRepository.findByEmail(email);

        if (worker != null && worker.getPassword().equals(password)) {
            String workerId = worker.getId();
            String sessionId = session.getId();

            // Store session info in Redis
            redisTemplate.opsForHash().put("activeWorkers", workerId, sessionId);
            session.setAttribute("workerId", workerId);

            Map<String, String> response = new HashMap<>();
            response.put("workerId", workerId);
            response.put("sessionId", sessionId);
            response.put("message", "Worker logged in successfully!");

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("message", "Invalid credentials!"));
        }
    }


    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        String workerId = (String) session.getAttribute("workerId");

        if (workerId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No worker is logged in!");
        }

//        redisTemplate.opsForSet().remove("activeWorkers", workerId);

        session.invalidate();

        return ResponseEntity.ok("Worker " + workerId + " logged out successfully!");
    }



    @GetMapping("/me")
    public ResponseEntity<String> validateSession(@RequestParam String sessionId) {
        // Check if the session ID exists in the activeWorkers hash
        String workerId = (String) redisTemplate.opsForHash().entries("activeWorkers").entrySet().stream()
                .filter(entry -> entry.getValue().equals(sessionId))
                .map(entry -> (String) entry.getKey())
                .findFirst()
                .orElse(null);

        if (workerId != null) {
            return ResponseEntity.ok(workerId);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session is invalid or expired");
        }
    }


    @GetMapping("/active")
    public Set<String> getActiveWorkers(HttpSession session) {

        Enumeration<String> attributeNames = session.getAttributeNames();
        return Collections.list(attributeNames).stream()
                .map(attr -> session.getAttribute(attr).toString())
                .collect(Collectors.toSet());
    }
    // ✅ New Method to Get Worker ID from Session
    @GetMapping("/worker-id")
    public ResponseEntity<String> getWorkerId(HttpSession session) {
        String workerId = (String) session.getAttribute("workerId");
        if (workerId != null) {
            return ResponseEntity.ok(workerId);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No worker is logged in!");
        }
    }

}