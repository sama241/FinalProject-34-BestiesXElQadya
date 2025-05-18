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

    @GetMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestParam String email, @RequestParam String password, HttpSession session) {
        Worker worker = workerRepository.findByEmail(email);

        if (worker != null && worker.getPassword().equals(password)) {

            session.setAttribute("workerId", worker.getId());

//            redisTemplate.opsForSet().add("activeWorkers", worker.getId());

            Map<String, String> response = new HashMap<>();
            response.put("workerId", worker.getId());
            response.put("message", "Worker logged in successfully!");

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("message", "Invalid credentials!"));
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
    public String currentWorker(HttpSession session) {
        Object workerId = session.getAttribute("workerId");
        if (workerId != null) {
            return "Logged in worker ID: " + workerId.toString();
        } else {
            return "No worker is logged in!";
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