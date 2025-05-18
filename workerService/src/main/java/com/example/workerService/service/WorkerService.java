package com.example.workerService.service;

import com.example.workerService.cache.WorkerCacheService;
import com.example.workerService.decorator.BasicWorkerProfile;
import com.example.workerService.decorator.EmergencyBadge;
import com.example.workerService.decorator.VerifiedBadge;
import com.example.workerService.decorator.WorkerProfile;
import com.example.workerService.model.Worker;
import com.example.workerService.repository.WorkerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WorkerService {

    private static final Logger logger = LoggerFactory.getLogger(WorkerService.class);

    @Autowired
    private WorkerRepository workerRepository;
    private final WorkerCacheService cacheService;

    @Autowired
    public WorkerService(WorkerCacheService cacheService) {
        this.cacheService = cacheService;
    }

    public void cacheWorker(Worker worker) {
        logger.info("Caching worker with ID: {}", worker.getId());
        cacheService.cacheWorker(worker, 1); // TTL = 10 minutes
    }

    public Worker getCachedWorker(String id) {
        logger.info("Fetching cached worker with ID: {}", id);
        return cacheService.getCachedWorker(id);
    }
    public boolean setWorkingHours(String workerId, List<Integer> newWorkingHours) {
        logger.info("Setting working hours for worker {}", workerId);
        Optional<Worker> workerOptional = workerRepository.findById(workerId);

        if (workerOptional.isPresent()) {
            Worker worker = workerOptional.get();
            worker.setAvailableHours(newWorkingHours);
            workerRepository.save(worker);
            logger.info("Updated working hours for worker {}", workerId);
            return true;
        } else {
            logger.warn("Worker with ID {} not found", workerId);
            return false;
        }
    }

    public Worker saveWorker(Worker worker) {
        logger.info("Saving new worker with ID: {}", worker.getId());
        return workerRepository.save(worker);
    }

    public String addBadgeToWorker(String workerId, String badgeType) {
        logger.info("Adding badge '{}' to worker {}", badgeType, workerId);
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
            }else {
                logger.error("Invalid badge type '{}' for worker {}", badgeType, workerId);
                throw new IllegalArgumentException("Unsupported badge type: " + badgeType);
            }

            workerRepository.save(worker);
            logger.info("Badge '{}' applied to worker {}", badgeType, workerId);

            return profile.getProfileDetails();
        } else {
            logger.error("Worker not found with id: {}", workerId);
            throw new RuntimeException("Worker not found with id: " + workerId);
        }
    }


    public Worker getWorkerById(String id) {
        logger.info("Getting worker by ID: {}", id);
        Worker worker = cacheService.getCachedWorker(id);

        if (worker != null) {
            logger.info("Cache HIT for worker {}", id);
            return worker;
        }

        logger.info("Cache MISS → loading from MongoDB for worker {}", id);
        worker = workerRepository.findById(id).orElse(null);

        if (worker != null) {
            cacheService.cacheWorker(worker, 10);
            logger.info("Worker {} cached after DB fetch", id);
        }

        return worker; // can still return null if not found
    }


    public void deleteCachedWorker(String id) {
        logger.info("Deleting cached worker {}", id);
        cacheService.deleteWorker(id);
    }

    public boolean addTimeSlots(String workerId, Integer timeSlot) {
        logger.info("Adding timeslot {} for worker {}", timeSlot, workerId);
        Optional<Worker> optional = workerRepository.findById(workerId);
        if (optional.isEmpty()) {
            logger.warn("Worker {} not found", workerId);
            return false;
        }

        Worker worker = optional.get();
        boolean added = worker.addAvailableHour(timeSlot);

        if (added) {
            workerRepository.save(worker);
            logger.info("Timeslot {} added for worker {}", timeSlot, workerId);
        } else {
            logger.info("Timeslot {} already exists for worker {}", timeSlot, workerId);
        }

        return added;
    }

    public boolean removeTimeSlots(String workerId, Integer timeSlot) {
        logger.info("Removing timeslot {} for worker {}", timeSlot, workerId);
        Optional<Worker> optional = workerRepository.findById(workerId);
        if (optional.isEmpty()) {
            logger.warn("Worker {} not found", workerId);
            return false;
        }

        Worker worker = optional.get();
        boolean removed = worker.removeAvailableHour(timeSlot);

        if (removed) {
            workerRepository.save(worker);
            logger.info("Timeslot {} removed for worker {}", timeSlot, workerId);
        } else {
            logger.info("Timeslot {} was not assigned to worker {}", timeSlot, workerId);
        }

        return removed;
    }





}
