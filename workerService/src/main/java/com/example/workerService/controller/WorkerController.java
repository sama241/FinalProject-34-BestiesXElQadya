package com.example.workerService.controller;
import com.example.workerService.factory.WorkerFactory;
import com.example.workerService.model.Worker;
import com.example.workerService.repository.WorkerRepository;

import com.example.workerService.service.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
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

    // 🔹 READ ALL
    @GetMapping
    public List<Worker> getAllWorkers() {
        return workerRepository.findAll();
    }

    // 🔹 READ ONE
    @GetMapping("/{id}")
    public Worker getWorkerById(@PathVariable String id) {
        return workerRepository.findById(id).orElse(null);
    }

    // 🔹 UPDATE
    @PutMapping("/{id}")
    public Worker updateWorker(@PathVariable String id, @RequestBody Worker updatedWorker) {
        Optional<Worker> optional = workerRepository.findById(id);
        if (optional.isPresent()) {
            Worker existing = optional.get();
            existing.setName(updatedWorker.getName());
            existing.setEmail(updatedWorker.getEmail());
            existing.setPassword(updatedWorker.getPassword());
            existing.setProfession(updatedWorker.getProfession());
            existing.setSkills(updatedWorker.getSkills());
            existing.setAvailableHours(updatedWorker.getAvailableHours());
            existing.setBadges(updatedWorker.getBadges());
            return workerRepository.save(existing);
        }
        return null;
    }

    // 🔹 DELETE
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


}
