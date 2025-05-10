package com.example.workerService.factory;

public class Carpenter implements WorkerProfileType {
    @Override
    public String getWorkerRole() {
        return "Carpenter: Builds and repairs wooden structures.";
    }
}
