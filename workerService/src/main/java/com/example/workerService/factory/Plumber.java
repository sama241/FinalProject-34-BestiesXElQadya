package com.example.workerService.factory;


public class Plumber implements WorkerProfileType {
    @Override
    public String getWorkerRole() {
        return "Plumber: Fixes and installs pipes.";
    }
}