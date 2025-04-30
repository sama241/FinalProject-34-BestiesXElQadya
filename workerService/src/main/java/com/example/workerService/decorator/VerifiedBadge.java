package com.example.workerService.decorator;


public class VerifiedBadge extends WorkerProfileDecorator {

    public VerifiedBadge(WorkerProfile workerProfile) {
        super(workerProfile);
    }

    @Override
    public String getProfileDetails() {
        return super.getProfileDetails() + " [Verified]";
    }
}
