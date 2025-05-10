package com.example.workerService.controller;


import com.example.workerService.model.Worker;
import com.example.workerService.repository.WorkerRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
            // Store worker ID in session
            session.setAttribute("workerId", worker.getId());

            // Add worker to the active set in Redis
            redisTemplate.opsForSet().add("activeWorkers", worker.getId());

            // Return worker ID and success message as JSON
            Map<String, String> response = new HashMap<>();
            response.put("workerId", worker.getId());
            response.put("message", "Worker logged in successfully!");

            return ResponseEntity.ok(response);
        } else {
            // Return error message as JSON
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("message", "Invalid credentials!"));
        }
    }


    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        String workerId = (String) session.getAttribute("workerId");

        if (workerId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No worker is logged in!");
        }

        // ✅ Remove from active list if present
        redisTemplate.opsForSet().remove("activeWorkers", workerId);

        // 🛑 Invalidate the session
        session.invalidate();

        // 👍 Successful logout
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
    public Set<String> getActiveWorkers() {
        return redisTemplate.opsForSet().members("activeWorkers");
    }

}