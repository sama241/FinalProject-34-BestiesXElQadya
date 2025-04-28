package com.example.workerService.decorator;

public class EmergencyBadge extends WorkerProfileDecorator {

    public EmergencyBadge(WorkerProfile workerProfile) {
        super(workerProfile);
    }

    @Override
    public String getProfileDetails() {
        return super.getProfileDetails() + " [Emergency Available]";
    }
}
