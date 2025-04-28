package com.example.workerService.repository;
import com.example.workerService.model.Worker;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkerRepository extends MongoRepository<Worker, String> {

    // Find worker by email (for Login/Logout functionality)
    Worker findByEmail(String email);
}
