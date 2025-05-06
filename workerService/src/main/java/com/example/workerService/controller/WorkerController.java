package com.example.workerService.controller;
import com.example.workerService.factory.WorkerFactory;
import com.example.workerService.model.Worker;
import com.example.workerService.repository.WorkerRepository;
import com.example.workerService.service.WorkerService;
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

    @PostMapping
    public Worker createWorker(@RequestBody Worker worker) {
        return workerRepository.save(worker);
    }

    // ✅ Get all Workers
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

    @DeleteMapping("/{id}")
    public String deleteWorker(@PathVariable String id) {
        if (workerRepository.existsById(id)) {
            workerRepository.deleteById(id);
            workerService.deleteCachedWorker(id);
            return "Worker deleted";
        }
        return "Worker not found";
    }


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

            Worker saved = workerRepository.save(existing);

            // 🔁 Refresh cache with updated worker
            workerService.cacheWorker(saved);

            return saved;
        }

        return null;
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

}
