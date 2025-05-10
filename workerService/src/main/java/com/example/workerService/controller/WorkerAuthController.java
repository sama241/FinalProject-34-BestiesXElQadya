package com.example.workerService.controller;


import com.example.workerService.model.Worker;
import com.example.workerService.repository.WorkerRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/worker/auth")
public class WorkerAuthController {

    @Autowired
    private WorkerRepository workerRepository;

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, HttpSession session) {
        Worker worker = workerRepository.findByEmail(email);

        if (worker != null && worker.getPassword().equals(password)) {
            session.setAttribute("workerId", worker.getId());
            return "Worker logged in successfully!";
        } else {
            return "Invalid credentials!";
        }
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "Worker logged out successfully!";
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
}