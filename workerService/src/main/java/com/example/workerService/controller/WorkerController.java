package com.example.workerService.controller;
//import com.example.workerService.factory.WorkerFactory;
import com.example.workerService.model.Worker;
import com.example.workerService.repository.WorkerRepository;

import com.example.workerService.service.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping
    public Worker createWorker(@RequestBody Worker worker) {
        return workerRepository.save(worker);
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
    public Worker updateWorker(@PathVariable String id, @RequestBody Worker updatedWorker) {
        Optional<Worker> optional = workerRepository.findById(id);
        if (optional.isPresent()) {
            Worker existing = optional.get();

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

            return workerRepository.save(existing);
        }
        return null;
    }


    // ✅ Delete Worker
    @DeleteMapping("/{id}")
    public String deleteWorker(@PathVariable String id) {
        if (workerRepository.existsById(id)) {
            workerRepository.deleteById(id);
            return "Worker deleted";
        }
        return "Worker not found";
    }


    @PutMapping("/workinghours/{id}")
    public String setWorkingHours(@PathVariable String id, @RequestBody List<Integer> newWorkingHours) {
        boolean updated = workerService.setWorkingHours(id, newWorkingHours);
        if (updated) {
            return "Working hours updated successfully!";
        } else {
            return "Worker not found.";
        }
    }

    @PutMapping("/{id}/addBadge")
    public String addBadge(@PathVariable String id, @RequestParam String badgeType) {
        return workerService.addBadgeToWorker(id, badgeType);
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



}
