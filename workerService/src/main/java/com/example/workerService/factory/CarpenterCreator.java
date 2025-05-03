package com.example.workerService.factory;

public class CarpenterCreator extends WorkerCreator {
    @Override
    protected WorkerProfileType createWorker() {
        return new Carpenter();
    }
}
