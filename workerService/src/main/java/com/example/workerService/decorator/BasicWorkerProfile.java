package com.example.workerService.decorator;

import com.example.workerService.model.Worker;

public class BasicWorkerProfile implements WorkerProfile {

    private Worker worker;

    public BasicWorkerProfile(Worker worker) {
        this.worker = worker;
    }

    @Override
    public String getProfileDetails() {
        return "Worker: " + worker.getName() + " (" + worker.getProfession() + ")";
    }
}