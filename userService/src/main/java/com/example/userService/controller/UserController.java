package com.example.userService.controller;

import com.example.userService.client.ReviewClient;
import com.example.userService.client.WorkerClient;
import com.example.userService.model.Favorite;
import com.example.userService.model.User;
import com.example.userService.client.BookingClient;
import com.example.userService.rabbitmq.UserProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.userService.service.UserService;
import jakarta.servlet.http.HttpSession;
import java.util.*;


@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private BookingClient bookingClient;

    @Autowired
    private WorkerClient workerClient;
    @Autowired
    private ReviewClient reviewClient;

    private  final UserProducer userProducer;

    public UserController(UserProducer userProducer) {
        this.userProducer = userProducer;
    }

    // get all
    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }

    // Create a new User
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.ok(createdUser);
    }

    // Get User by ID
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUser(@PathVariable UUID userId) {
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    // Get User by Username
    @GetMapping("/username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        Optional<User> optionalUser = userService.getByUsername(username);

        if (optionalUser.isPresent()) {
            return ResponseEntity.ok(optionalUser.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Get User by Email
    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        User optionalUser = userService.getByEmail(email);

        if (optionalUser!=null) {
            return ResponseEntity.ok(optionalUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Get User by Phone
    @GetMapping("/phone/{phone}")
    public ResponseEntity<User> getUserByPhone(@PathVariable String phone) {
        Optional<User> optionalUser = userService.getByPhone(phone);

        if (optionalUser.isPresent()) {
            return ResponseEntity.ok(optionalUser.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Update User details
    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable UUID userId, @RequestBody User user) {


        User updatedUser = userService.updateUser(userId, user);
        return ResponseEntity.ok(updatedUser);
    }

    // Delete User by ID
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {

        userService.deleteUser(userId);
        userProducer.sendDeleteToReview(userId+"");
        return ResponseEntity.ok().build();
    }


    // Favorite Worker Functions

//    @PostMapping("/{userId}/favorites")
//    public ResponseEntity<String> addFavoriteWorker(@RequestBody Favorite favorite) {
//        System.out.println("favourite" + favorite);
//        Favorite result = userService.addFavoriteWorker(favorite);
//
//        if (result == null) {
//            return ResponseEntity.status(HttpStatus.CONFLICT).body("Favorite worker already exists.");
//        }
//
//        return ResponseEntity.status(HttpStatus.CREATED).body("Favorite worker added successfully.");
//    }

    @PostMapping("/{userId}/favorites")
    public ResponseEntity<String> addFavoriteWorker(@PathVariable UUID userId, @RequestBody Map<String, String> body) {
        String workerId = body.get("workerId");

        if (workerId == null || workerId.isEmpty()) {
            return ResponseEntity.badRequest().body("Missing workerId in request body.");
        }

        // Create new Favorite using userId from path and workerId from body
        Favorite favorite = new Favorite();
        favorite.setUserId(userId);
        favorite.setWorkerId(workerId);

        // CHECK ON WORKER ID BY CLIENT
        ResponseEntity<Map<String, Object>> worker = workerClient.getWorkerById(favorite.getWorkerId());

        if (worker.getStatusCode() != HttpStatus.OK) {
            // Handle other unexpected status codes if necessary
            return ResponseEntity.badRequest().body("Invalid worker ID: " + favorite.getWorkerId());
        }

//        if (worker == null || worker.isEmpty()) {
//            return ResponseEntity.badRequest().body("Invalid worker ID: " + favorite.getWorkerId());
//        }

        // CHECK ON USER ID BY SERVICE
        if(!userService.existsByUserId(favorite.getUserId())){
            return ResponseEntity.badRequest().body("Invalid user ID: " + favorite.getUserId());
        }

        Favorite result = userService.addFavoriteWorker(favorite);

        if (result == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Favorite worker already exists.");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body("Favorite worker added successfully.");
    }




    // Remove Worker from Favorites
    @DeleteMapping("/favorites")
    public ResponseEntity<String> removeFavoriteWorker(@RequestBody Favorite favorite) {
        String workerId = favorite.getWorkerId();
        UUID userId = favorite.getUserId();
        System.out.println("the user is " + userId);
        System.out.println("the worker is " + workerId);

        String result = userService.removeFavoriteWorker(userId, workerId);

        if (result.equals("Favorite worker not found.")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }

        return ResponseEntity.ok(result);
    }

    // Get Favorite Workers
    //the error is hereeeeeeeee
    @GetMapping("/{userId}/favorites")
    public ResponseEntity< List<Map<String, Object>>> getFavoriteWorkers(@PathVariable UUID userId) {
        List<Favorite> favorites = userService.getFavoriteWorkers(userId);

        List<Map<String, Object>> favoriteWorkersInfo = new ArrayList<>();

        // map the worker ids to the info of worker, using the worker client accordingly
        for (Favorite favorite : favorites) {
            // Assuming each Favorite has a workerId, you can call WorkerClient to get worker info
            ResponseEntity<Map<String, Object>> worker = workerClient.getWorkerById(favorite.getWorkerId().toString());

            favoriteWorkersInfo.add(worker.getBody());
        }
        return ResponseEntity.ok(favoriteWorkersInfo);
    }


    @GetMapping("/{userId}/bookings")
    public ResponseEntity<List<Map<String, Object>>>  getUserBookings(@PathVariable String userId) {
        List<Map<String, Object>>  bookings = bookingClient.getBookingsByUserId(userId);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/{userId}/reviews")
    public ResponseEntity<List<Map<String, Object>>> getReviewsByUserId(@PathVariable String userId) {
        List<Map<String, Object>>  bookings = reviewClient.getReviewsByUserId(userId);
        return ResponseEntity.ok(bookings);
    }







}