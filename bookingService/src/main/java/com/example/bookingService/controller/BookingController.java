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


    @GetMapping("/get/all")
    public List<Booking> getAll() {

        return bookingService.findAll();
    }

    @GetMapping("/get/{id}")
    public Booking getById(@PathVariable Long id) {

        return bookingRepository.findById(id).orElseThrow();
    }

    @GetMapping("/get/user/{userId}")
    public ResponseEntity<List<Booking>> getBookingsByUserId(@PathVariable String userId) {

        List<Booking> bookings = bookingService.getBookingsByUserId(userId);
        return ResponseEntity.ok(bookings);
    }


    @PostMapping("/user/create")
    public ResponseEntity<?> create(@RequestBody Booking booking, @RequestHeader("X-User-Id") String userId) {


        int hour = booking.getTimeslot().getHour();
        System.out.println("HOUR"+hour);
        String result = workerClient.removeTimeSlot(booking.getWorkerId(), hour);

        if (!result.toLowerCase().contains("success")) {
            return ResponseEntity.badRequest().body("Selected hour is not available.");
        }

        booking.setUserId(userId);
        booking.setStatus(BookingStatus.CONFIRMED);
        return ResponseEntity.ok(bookingService.save(booking));
    }

    @PutMapping("/user/{id}/reschedule")
    public ResponseEntity<?> rescheduleBooking(@RequestHeader("X-User-Id") String userId, @PathVariable Long id, @RequestParam String newTime) {


        Booking booking = bookingService.findById(id);
        LocalDateTime parsed = LocalDateTime.parse(newTime);
        dispatcher.reschedule("reschedule", booking, parsed);

        bookingService.save(booking);
        return ResponseEntity.ok("Booking rescheduled successfully");
    }

    @PutMapping("/user/{id}/cancel")
    public ResponseEntity<?> cancelBooking(@RequestHeader("X-User-Id") String userId, @PathVariable Long id) {


        Booking booking = bookingService.findById(id);
        dispatcher.cancel("cancel", booking);

        bookingService.save(booking);
        return ResponseEntity.ok("Booking cancelled successfully");
    }

    @PutMapping("/user/{id}/update")
    public Booking update(@PathVariable Long id, @RequestBody Booking booking) {

        booking.setId(id);
        return bookingRepository.save(booking);
    }

    @DeleteMapping("/user/{id}/delete")
    public void delete(@PathVariable Long id) {
        Booking booking = bookingService.findById(id);
        if(booking.getStatus() == BookingStatus.CANCELLED) {
            bookingRepository.deleteById(id);
        }

    }

    @GetMapping("/get/worker/{workerId}")
    public ResponseEntity<List<Booking>> getBookingsByWorkerId(@PathVariable String workerId) {

        List<Booking> bookings = bookingService.getBookingsByWorkerId(workerId);
        return ResponseEntity.ok(bookings);
    }

    @PutMapping("/worker/{id}/status")
    public ResponseEntity<?> updateBookingStatus(@PathVariable Long id) {

        Booking booking = bookingService.findById(id);
        booking.setStatus(BookingStatus.IN_PROGRESS);
        bookingService.save(booking);

        return ResponseEntity.ok("Booking status updated to " + booking.getStatus());
    }
}