//package com.example.workerService.client;
//
//import com.example.reviewService.model.Review;
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//
//import java.util.List;
//
//@FeignClient(name = "review-service", url = "http://review-service:8084/reviews")
//public interface ReviewClient {
//    @GetMapping("/worker/{workerId}")
//    List<Review> getReviewsByWorkerId(@PathVariable("workerId") String workerId);
//
//    @GetMapping("/api/reviews/worker/{workerId}/average")
//    double getAverageRating(@PathVariable String workerId);
//}