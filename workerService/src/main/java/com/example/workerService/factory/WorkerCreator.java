package com.example.workerService.factory;

public abstract class WorkerCreator {
    public WorkerProfileType hireWorker() {
        return createWorker();
    }

    protected abstract WorkerProfileType createWorker();
}