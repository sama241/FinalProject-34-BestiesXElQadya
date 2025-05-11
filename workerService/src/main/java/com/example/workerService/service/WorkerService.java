package com.example.workerService.service;
import com.example.workerService.cache.WorkerCacheService;
import com.example.workerService.decorator.BasicWorkerProfile;
import com.example.workerService.decorator.EmergencyBadge;
import com.example.workerService.decorator.VerifiedBadge;
import com.example.workerService.decorator.WorkerProfile;
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
    private final WorkerCacheService cacheService;

    @Autowired
    public WorkerService(WorkerCacheService cacheService) {
        this.cacheService = cacheService;
    }

    public void cacheWorker(Worker worker) {
        cacheService.cacheWorker(worker, 1); // TTL = 10 minutes
    }

    public Worker getCachedWorker(String id) {
        return cacheService.getCachedWorker(id);
    }
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


            return profile.getProfileDetails();
        } else {
            throw new RuntimeException("Worker not found with id: " + workerId);
        }
    }


    public Worker getWorkerById(String id) {
        // First check Redis
        Worker worker = cacheService.getCachedWorker(id);

        if (worker != null) {
            System.out.println(" Cache HIT");
            return worker;
        }

        System.out.println(" Cache MISS → loading from MongoDB");

        // Fallback to MongoDB
        worker = workerRepository.findById(id).orElse(null);

        if (worker != null) {
            cacheService.cacheWorker(worker, 10); // cache for 10 minutes
        }

        return worker;
    }


    public void deleteCachedWorker(String id) {
        cacheService.deleteWorker(id);
    }

    public boolean addTimeSlots(String workerId, Integer timeSlot) {
        Optional<Worker> optional = workerRepository.findById(workerId);
        if (optional.isEmpty()) return false;

        Worker worker = optional.get();
        boolean added = worker.addAvailableHour(timeSlot);

        if (added) {
            workerRepository.save(worker);
        }

        return added;
    }

    public boolean removeTimeSlots(String workerId, Integer timeSlot) {
        Optional<Worker> optional = workerRepository.findById(workerId);
        if (optional.isEmpty()) return false;

        Worker worker = optional.get();
        boolean removed = worker.removeAvailableHour(timeSlot);

        if (removed) {
            workerRepository.save(worker);
        }

        return removed;
    }





}