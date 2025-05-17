package com.example.bookingService.controller;

import com.example.bookingService.client.WorkerClient;
import com.example.bookingService.command.BookingDispatcher;
import com.example.bookingService.model.Booking;
import com.example.bookingService.model.BookingStatus;
import com.example.bookingService.repository.BookingRepository;
import com.example.bookingService.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingRepository bookingRepository;


    @GetMapping
    public List<Booking> getAll() {
        return bookingService.findAll();

    }


    @GetMapping("/{id}")
    public Booking getById(@PathVariable Long id) {
        return bookingRepository.findById(id).orElseThrow();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Booking>> getBookingsByUserId(@PathVariable String userId) {
        List<Booking> bookings = bookingService.getBookingsByUserId(userId);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/worker/{workerId}")
    public ResponseEntity<List<Booking>> getBookingsByWorkerId(@PathVariable String workerId) {
        List<Booking> bookings = bookingService.getBookingsByWorkerId(workerId);
        return ResponseEntity.ok(bookings);
    }


    @Autowired
    private BookingService bookingService;

    @Autowired
    private WorkerClient workerClient;

    @Autowired
    private BookingDispatcher dispatcher;



    @PostMapping
    public ResponseEntity<?> create(@RequestBody Booking booking, HttpSession session) {
        String userId = (String) session.getAttribute("userId");  // Get userId from session
        if (userId == null) {
            return ResponseEntity.status(401).body("User not logged in");
        }

        int hour = booking.getTimeslot().getHour();
        // Try to "book" by removing a time slot
        String result = workerClient.removeTimeSlot(booking.getWorkerId(), hour);

        if (!result.toLowerCase().contains("success")) {
            return ResponseEntity.badRequest().body("Selected hour is not available.");
        }

        booking.setUserId(userId);  // Set the userId from session
        booking.setStatus(BookingStatus.CONFIRMED);
        return ResponseEntity.ok(bookingService.save(booking));
    }


    @PutMapping("/{id}/reschedule")
    public ResponseEntity<?> rescheduleBooking(@PathVariable Long id,
                                               @RequestParam String newTime) {
        Booking booking = bookingService.findById(id);

        if (newTime == null) {
            return ResponseEntity.badRequest().body("newTime is required");
        }

        LocalDateTime parsed = LocalDateTime.parse(newTime);
        dispatcher.reschedule("reschedule", booking, parsed);

        bookingService.save(booking);
        return ResponseEntity.ok("Booking rescheduled successfully");
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancelBooking(@PathVariable Long id) {
        Booking booking = bookingService.findById(id);

        dispatcher.cancel("cancel", booking);

        bookingService.save(booking);
        return ResponseEntity.ok("Booking cancelled successfully");
    }


    @PutMapping("/{id}")
    public Booking update(@PathVariable Long id, @RequestBody Booking booking) {
        booking.setId(id);
        return bookingRepository.save(booking);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        bookingRepository.deleteById(id);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateBookingStatus(@PathVariable Long id) {
        Booking booking = bookingService.findById(id);
        BookingStatus status= BookingStatus.IN_PROGRESS;

        booking.setStatus(status);
        bookingService.save(booking);

        return ResponseEntity.ok("Booking status updated to " + status);
    }





}
