package com.example.workerService.controller;
import com.example.workerService.factory.WorkerFactoryDispatcher;
import com.example.workerService.factory.WorkerProfileType;
import com.example.workerService.model.Worker;
import com.example.workerService.repository.WorkerRepository;

import com.example.workerService.service.WorkerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/workers")
public class WorkerController {

    @Autowired
    private WorkerRepository workerRepository;
    @Autowired
    private WorkerService workerService;
    // ✅ Create new Worker
//    @PostMapping("/create")
//    public Worker createWorker22(@RequestBody Worker worker) {
//        return workerRepository.save(worker);
//    }

    @PostMapping("/create")
    public Worker createWorker(@RequestBody Worker workerRequest) {

        WorkerProfileType profile = WorkerFactoryDispatcher.getWorkerProfile(workerRequest.getProfession());
        System.out.println("Hiring: " + profile.getWorkerRole()); // Optional log

        // Manually create the worker using the info and factory output
        Worker worker = new Worker(
                workerRequest.getName(),
                workerRequest.getEmail(),
                workerRequest.getPassword(),
                workerRequest.getProfession(),
                workerRequest.getSkills(),
                workerRequest.getAvailableHours()
        );

        return workerService.saveWorker(worker);
    }

    // ✅ Get all Workers
    @GetMapping
    public List<Worker> getAllWorkers() {
        return workerRepository.findAll();
    }

    // ✅ Get Worker by ID
    @GetMapping("/{id}")
    public Worker getWorkerById(@PathVariable String id) {
        return workerRepository.findById(id).orElse(null);
    }

    // ✅ Update Worker
    @PutMapping("/{id}")
    public ResponseEntity<?> updateWorker(@PathVariable String id, @RequestBody Worker updatedWorker, HttpSession session) {
        String sessionWorkerId = (String) session.getAttribute("workerId");

        if (sessionWorkerId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Please login first.");
        }

        if (!sessionWorkerId.equals(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only update your own profile.");
        }

        Optional<Worker> optional = workerRepository.findById(id);
        if (optional.isPresent()) {
            Worker existing = optional.get();

            if (updatedWorker.getName() != null) existing.setName(updatedWorker.getName());
            if (updatedWorker.getEmail() != null) existing.setEmail(updatedWorker.getEmail());
            if (updatedWorker.getPassword() != null) existing.setPassword(updatedWorker.getPassword());
            if (updatedWorker.getProfession() != null) existing.setProfession(updatedWorker.getProfession());
            if (updatedWorker.getSkills() != null) existing.setSkills(updatedWorker.getSkills());
            if (updatedWorker.getAvailableHours() != null) existing.setAvailableHours(updatedWorker.getAvailableHours());
            if (updatedWorker.getBadges() != null) existing.setBadges(updatedWorker.getBadges());

            return ResponseEntity.ok(workerRepository.save(existing));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteWorker(@PathVariable String id, HttpSession session) {
        String sessionWorkerId = (String) session.getAttribute("workerId");

        if (sessionWorkerId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Please login first.");
        }

        if (!sessionWorkerId.equals(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only delete your own profile.");
        }

        if (workerRepository.existsById(id)) {
            workerRepository.deleteById(id);
            return ResponseEntity.ok("Worker deleted");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Worker not found");
    }

    @PutMapping("/workinghours/{id}")
    public ResponseEntity<String> setWorkingHours(@PathVariable String id, @RequestBody List<Integer> newWorkingHours, HttpSession session) {
        String sessionWorkerId = (String) session.getAttribute("workerId");

        if (sessionWorkerId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Please login first.");
        }

        if (!sessionWorkerId.equals(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only update your own working hours.");
        }

        boolean updated = workerService.setWorkingHours(id, newWorkingHours);
        if (updated) {
            return ResponseEntity.ok("Working hours updated successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Worker not found.");
        }
    }

    @PutMapping("/{id}/addBadge")
    public ResponseEntity<String> addBadge(@PathVariable String id, @RequestParam String badgeType, HttpSession session) {
        String sessionWorkerId = (String) session.getAttribute("workerId");

        if (sessionWorkerId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Please login first.");
        }

        if (!sessionWorkerId.equals(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only update your own badges.");
        }

        String result = workerService.addBadgeToWorker(id, badgeType);
        return ResponseEntity.ok(result);
    }
}
