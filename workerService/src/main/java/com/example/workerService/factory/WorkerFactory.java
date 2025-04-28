package com.example.workerService.factory;
import com.example.workerService.model.Worker;
import java.util.List;

public class WorkerFactory {

    public static Worker createWorker(String name, String email, String password, String profession, List<String> skills, List<Integer> availableHours) {
        Worker worker = new Worker();
        worker.setName(name);
        worker.setEmail(email);
        worker.setPassword(password);
        worker.setProfession(profession);
        worker.setSkills(skills);
        worker.setAvailableHours(availableHours);
        return worker;
    }
}
