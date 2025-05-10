package com.example.workerService.factory;

public class WorkerFactoryDispatcher {

    public static WorkerProfileType getWorkerProfile(String profession) {
        switch (profession.toLowerCase()) {
            case "plumber":
                return new PlumberCreator().hireWorker();
            case "electrician":
                return new ElectricianCreator().hireWorker();
            case "carpenter":
                return new CarpenterCreator().hireWorker();
            case "mechanic":
                return new MechanicCreator().hireWorker();
            default:
                throw new IllegalArgumentException("Unknown profession: " + profession);
        }
    }
}
