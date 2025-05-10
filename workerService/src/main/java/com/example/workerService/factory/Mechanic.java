package com.example.workerService.factory;

public class Mechanic implements WorkerProfileType {
    @Override
    public String getWorkerRole() {
        return "Mechanic: Repairs vehicles and machinery.";
    }
}
