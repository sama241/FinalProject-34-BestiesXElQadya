package com.example.workerService.decorator;

public abstract class WorkerProfileDecorator implements WorkerProfile {

    protected WorkerProfile workerProfile;

    public WorkerProfileDecorator(WorkerProfile workerProfile) {
        this.workerProfile = workerProfile;
    }

    @Override
    public String getProfileDetails() {
        return workerProfile.getProfileDetails();
    }
}
