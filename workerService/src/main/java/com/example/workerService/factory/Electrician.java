package com.example.workerService.factory;

public class Electrician implements WorkerProfileType {
    @Override
    public String getWorkerRole() {
        return "Electrician: Installs and repairs electrical systems.";
    }
}
