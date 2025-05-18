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
import java.util.Map;
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

    @GetMapping
    public List<Worker> getAllWorkers() {
        return workerRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getWorker(@PathVariable String id) {
        Worker worker = workerService.getWorkerById(id);
        if (worker == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Worker not found.");
        }
        return ResponseEntity.ok(worker);
    }

    @PutMapping("/{id}")
    public Worker updateWorker(@PathVariable String id, @RequestBody Worker updatedWorker, HttpSession session) {

        // 🛡️ Check if the user is logged in
        String sessionWorkerId = (String) session.getAttribute("workerId");
        if (sessionWorkerId == null || !sessionWorkerId.equals(id)) {
            throw new RuntimeException("Unauthorized: You can only update your own profile.");
        }

        // 🔍 Find the existing worker
        Optional<Worker> optional = workerRepository.findById(id);
        if (optional.isPresent()) {
            Worker existing = optional.get();

            // ✅ Update only the provided fields
            if (updatedWorker.getName() != null) {
                existing.setName(updatedWorker.getName());
            }

            if (updatedWorker.getEmail() != null) {
                existing.setEmail(updatedWorker.getEmail());
            }

            if (updatedWorker.getPassword() != null) {
                existing.setPassword(updatedWorker.getPassword());
            }

            if (updatedWorker.getProfession() != null) {
                existing.setProfession(updatedWorker.getProfession());
            }

            if (updatedWorker.getSkills() != null) {
                existing.setSkills(updatedWorker.getSkills());
            }

            if (updatedWorker.getAvailableHours() != null) {
                existing.setAvailableHours(updatedWorker.getAvailableHours());
            }

            if (updatedWorker.getBadges() != null) {
                existing.setBadges(updatedWorker.getBadges());
            }

            // 💾 Save the updated worker
            Worker saved = workerRepository.save(existing);

            // 🔁 Refresh cache with updated worker
            workerService.cacheWorker(saved);

            return saved;
        }

        throw new RuntimeException("Worker not found.");
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
    @PostMapping("/cache")
    public ResponseEntity<String> cacheWorker(@RequestBody Worker worker) {
        workerService.cacheWorker(worker);
        return ResponseEntity.ok("Worker cached for 10 minutes.");
    }

    @GetMapping("/cache/{id}")
    public ResponseEntity<?> getCachedWorker(@PathVariable String id) {
        Worker worker = workerService.getCachedWorker(id);
        if (worker == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Worker not in cache or TTL expired.");
        }
        return ResponseEntity.ok(worker);
    }

    @PutMapping("/{workerId}/add-timeslot")
    public ResponseEntity<String> addTimeSlot(@PathVariable String workerId, @RequestParam int hour) {
        boolean result = workerService.addTimeSlots(workerId, hour);
        if (result) {
            return ResponseEntity.ok("Hour added successfully.");
        } else {
            return ResponseEntity.badRequest().body("Hour already exists or worker not found.");
        }
    }

    // ➖ Remove time slot (hour) from worker
    @PutMapping("/{workerId}/remove-timeslot")
    public ResponseEntity<String> removeTimeSlot(@PathVariable String workerId, @RequestParam int hour) {
        boolean result = workerService.removeTimeSlots(workerId, hour);
        if (result) {
            return ResponseEntity.ok("Hour removed successfully.");
        } else {
            return ResponseEntity.badRequest().body("Hour not found or worker not found.");
        }
    }

    @PutMapping("/{workerId}/average-rating")
    public ResponseEntity<?> updateAverageRating(@PathVariable String workerId, @RequestBody double newAverageRating) {
        return workerRepository.findById(workerId)
                .map(worker -> {
                    worker.setAverageRating(newAverageRating);
                    workerRepository.save(worker);  // ⚠️ MUST save after update!
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Worker not found"));
    }


}
