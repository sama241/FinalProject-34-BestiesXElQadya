package com.example.workerService.service;
import com.example.workerService.decorator.BasicWorkerProfile;
import com.example.workerService.decorator.EmergencyBadge;
import com.example.workerService.decorator.VerifiedBadge;
import com.example.workerService.decorator.WorkerProfile;
import com.example.workerService.model.Worker;
import com.example.workerService.repository.WorkerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class WorkerService {

    private static final Logger logger = LoggerFactory.getLogger(WorkerService.class);

    @Autowired
    private WorkerRepository workerRepository;

    @Cacheable(value = "worker_cache", key = "#id")
    public Worker getWorkerById(String id) {
        logger.info("Cache MISS → loading from DB for worker {}", id);
        return workerRepository.findById(id).orElse(null);
    }
    @CacheEvict(value = "worker_cache", key = "#id")
    public void deleteCachedWorker(String id) {
        logger.info("Deleting cached worker {}", id);
        workerRepository.deleteById(id);
    }

    @CachePut(value = "worker_cache", key = "#result.id")
    public Worker saveWorker(Worker worker) {
        logger.info("Saving new worker with ID: {}", worker.getId());
        return workerRepository.save(worker);
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


    public boolean updateAverageRating(String workerId, double newRating) {
        Optional<Worker> optional = workerRepository.findById(workerId);
        if (optional.isEmpty()) {
            return false;
        }

        Worker worker = optional.get();
        worker.setAverageRating(newRating);
        workerRepository.save(worker);
        return true;
    }


}
