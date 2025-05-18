package com.example.bookingService.controller;

import com.example.bookingService.client.UserClient;
import com.example.bookingService.client.WorkerClient;
import com.example.bookingService.command.BookingDispatcher;
import com.example.bookingService.model.Booking;
import com.example.bookingService.model.BookingStatus;
import com.example.bookingService.repository.BookingRepository;
import com.example.bookingService.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private WorkerClient workerClient;

    @Autowired
    private UserClient userClient;

    @Autowired
    private BookingDispatcher dispatcher;

    // Validate the session and return the user ID
    private String validateSession(String sessionId) {
        ResponseEntity<String> response = userClient.validateSession(sessionId);
        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        }
        throw new IllegalStateException("Invalid session");
    }

    @GetMapping
    public List<Booking> getAll(@RequestParam String sessionId) {
        validateSession(sessionId);
        return bookingService.findAll();
    }

    @GetMapping("/{id}")
    public Booking getById(@PathVariable Long id, @RequestParam String sessionId) {
        validateSession(sessionId);
        return bookingRepository.findById(id).orElseThrow();
    }

    @GetMapping("/user")
    public ResponseEntity<List<Booking>> getBookingsByUserId(@RequestParam String sessionId) {
        String userId = validateSession(sessionId);
        List<Booking> bookings = bookingService.getBookingsByUserId(userId);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/worker/{workerId}")
    public ResponseEntity<List<Booking>> getBookingsByWorkerId(@PathVariable String workerId, @RequestParam String sessionId) {
        validateSession(sessionId);
        List<Booking> bookings = bookingService.getBookingsByWorkerId(workerId);
        return ResponseEntity.ok(bookings);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Booking booking, @RequestParam String sessionId) {
        String userId = validateSession(sessionId);

        int hour = booking.getTimeslot().getHour();
        String result = workerClient.removeTimeSlot(booking.getWorkerId(), hour);

        if (!result.toLowerCase().contains("success")) {
            return ResponseEntity.badRequest().body("Selected hour is not available.");
        }

        booking.setUserId(userId);
        booking.setStatus(BookingStatus.CONFIRMED);
        return ResponseEntity.ok(bookingService.save(booking));
    }

    @PutMapping("/{id}/reschedule")
    public ResponseEntity<?> rescheduleBooking(@PathVariable Long id, @RequestParam String newTime, @RequestParam String sessionId) {
        validateSession(sessionId);

        Booking booking = bookingService.findById(id);
        LocalDateTime parsed = LocalDateTime.parse(newTime);
        dispatcher.reschedule("reschedule", booking, parsed);

        bookingService.save(booking);
        return ResponseEntity.ok("Booking rescheduled successfully");
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancelBooking(@PathVariable Long id, @RequestParam String sessionId) {
        validateSession(sessionId);

        Booking booking = bookingService.findById(id);
        dispatcher.cancel("cancel", booking);

        bookingService.save(booking);
        return ResponseEntity.ok("Booking cancelled successfully");
    }

    @PutMapping("/{id}")
    public Booking update(@PathVariable Long id, @RequestBody Booking booking, @RequestParam String sessionId) {
        validateSession(sessionId);
        booking.setId(id);
        return bookingRepository.save(booking);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id, @RequestParam String sessionId) {
        validateSession(sessionId);
        bookingRepository.deleteById(id);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateBookingStatus(@PathVariable Long id, @RequestParam String sessionId) {
        validateSession(sessionId);
        Booking booking = bookingService.findById(id);
        booking.setStatus(BookingStatus.IN_PROGRESS);
        bookingService.save(booking);

        return ResponseEntity.ok("Booking status updated to " + booking.getStatus());
    }
}
