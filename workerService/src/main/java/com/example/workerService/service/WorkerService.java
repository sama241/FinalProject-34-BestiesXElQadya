package com.example.workerService.service;

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


}

