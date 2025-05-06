package com.example.workerService.factory;

public class MechanicCreator extends WorkerCreator {
    @Override
    protected WorkerProfileType createWorker() {
        return new Mechanic();
    }
}
