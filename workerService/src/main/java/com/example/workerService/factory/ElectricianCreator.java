package com.example.workerService.factory;

public class ElectricianCreator extends WorkerCreator {
    @Override
    protected WorkerProfileType createWorker() {
        return new Electrician();
    }
}
