package com.example.workerService.factory;

public class PlumberCreator extends WorkerCreator {
    @Override
    protected WorkerProfileType createWorker() {
        return new Plumber();
    }
}
