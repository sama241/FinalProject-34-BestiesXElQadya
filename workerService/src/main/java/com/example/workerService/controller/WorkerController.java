package com.example.workerService.controller;

import com.example.workerService.client.BookingClient;
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
import java.util.UUID;

@RestController
@RequestMapping("/workers")
public class WorkerController {

    @Autowired
    private WorkerRepository workerRepository;
    @Autowired
    private WorkerService workerService;
    @Autowired
    private BookingClient bookingClient;

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
                workerRequest.getLocation(),
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

    @PutMapping("/update")
    public Worker updateWorker(@RequestHeader("X-Worker-Id") String workerId, @RequestBody Worker updatedWorker) {



        // 🔍 Find the existing worker
        Optional<Worker> optional = workerRepository.findById(workerId);
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


    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteWorker(@RequestHeader("X-Worker-Id") String workerId) {

        if (workerRepository.existsById(workerId)) {
            workerRepository.deleteById(workerId);
            return ResponseEntity.ok("Worker deleted");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Worker not found");
    }

    @PutMapping("/workinghours")
    public ResponseEntity<String> setWorkingHours(@RequestHeader("X-Worker-Id") String workerId, @RequestBody List<Integer> newWorkingHours) {

        boolean updated = workerService.setWorkingHours(workerId, newWorkingHours);
        if (updated) {
            return ResponseEntity.ok("Working hours updated successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Worker not found.");
        }
    }

    @PutMapping("/addBadge")
    public ResponseEntity<String> addBadge(
            @RequestHeader("X-Worker-Id") String workerId,
            @RequestParam String badgeType) {

        // Add the badge to the worker
        String result = workerService.addBadgeToWorker(workerId, badgeType);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/cache")
    public ResponseEntity<String> cacheWorker(@RequestBody Worker worker) {
        workerService.cacheWorker(worker);
        return ResponseEntity.ok("Worker cached for 10 minutes.");
    }

    @GetMapping("/cache")
    public ResponseEntity<?> getCachedWorker(@RequestHeader("X-Worker-Id") String workerId) {
        Worker worker = workerService.getCachedWorker(workerId);
        if (worker == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Worker not in cache or TTL expired.");
        }
        return ResponseEntity.ok(worker);
    }

    @PutMapping("/add-timeslot")
    public ResponseEntity<String> addTimeSlot(@RequestHeader("X-Worker-Id") String workerId, @RequestParam int hour) {
        boolean result = workerService.addTimeSlots(workerId, hour);
        if (result) {
            return ResponseEntity.ok("Hour added successfully.");
        } else {
            return ResponseEntity.badRequest().body("Hour already exists or worker not found.");
        }
    }

    // ➖ Remove time slot (hour) from worker
    @PutMapping("/remove-timeslot")
    public ResponseEntity<String> removeTimeSlot(@RequestHeader("X-Worker-Id") String workerId, @RequestParam int hour) {
        boolean result = workerService.removeTimeSlots(workerId, hour);
        if (result) {
            return ResponseEntity.ok("Hour removed successfully.");
        } else {
            return ResponseEntity.badRequest().body("Hour not found or worker not found.");
        }
    }

    @GetMapping("/bookings")
    public ResponseEntity<List<Map<String, Object>>> getWorkerBookings(@RequestHeader("X-Worker-Id") String workerId) {
        List<Map<String, Object>> bookings = bookingClient.getBookingsByWorkerId(workerId);
        return ResponseEntity.ok(bookings);
    }

}
