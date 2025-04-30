
package com.example.workerService.service;

import com.example.workerService.decorator.BasicWorkerProfile;
import com.example.workerService.decorator.EmergencyBadge;
import com.example.workerService.decorator.VerifiedBadge;
import com.example.workerService.decorator.WorkerProfile;
import com.example.workerService.factory.WorkerFactory;
import com.example.workerService.model.Worker;
import com.example.workerService.repository.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WorkerService {

    @Autowired
    private WorkerRepository workerRepository;

    public boolean setWorkingHours(String workerId, List<Integer> newWorkingHours) {
        Optional<Worker> workerOptional = workerRepository.findById(workerId);

        if (workerOptional.isPresent()) {
            Worker worker = workerOptional.get();
            worker.setAvailableHours(newWorkingHours);
            workerRepository.save(worker);
            return true;
        } else {
            return false;
        }
    }

    public Worker saveWorker(Worker worker) {
        return workerRepository.save(worker);
    }

    public String addBadgeToWorker(String workerId, String badgeType) {
        Optional<Worker> workerOptional = workerRepository.findById(workerId);

        if (workerOptional.isPresent()) {
            Worker worker = workerOptional.get();

            WorkerProfile profile = new BasicWorkerProfile(worker);

            // Apply the correct badge based on the badgeType parameter
            if ("verified".equalsIgnoreCase(badgeType)) {
                profile = new VerifiedBadge(profile);
                worker.addBadge("Verified");
            } else if ("emergency".equalsIgnoreCase(badgeType)) {
                profile = new EmergencyBadge(profile);
                worker.addBadge("Emergency Available");
            }

            workerRepository.save(worker);  // Save worker to MongoDB with updated badges

            // 🚨🚨 RETURN the decorated profile details
            return profile.getProfileDetails();
        } else {
            throw new RuntimeException("Worker not found with id: " + workerId);
        }
    }}
