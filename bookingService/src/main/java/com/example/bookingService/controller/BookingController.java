package com.example.bookingService.controller;

import com.example.bookingService.client.UserClient;
import com.example.bookingService.client.WorkerClient;
import com.example.bookingService.command.BookingDispatcher;
import com.example.bookingService.model.Booking;
import com.example.bookingService.model.BookingStatus;
import com.example.bookingService.repository.BookingRepository;
import com.example.bookingService.service.BookingService;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
        System.out.println("HOUR " + hour);

        try {
            // Call the workerClient to remove the time slot
            String result = workerClient.removeTimeSlot(booking.getWorkerId(), hour);

            // If the result doesn't indicate success, throw ResponseStatusException
            if (result == null || !result.toLowerCase().contains("success")) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Selected hour is not available.");
            }

            // Proceed with booking creation if the time slot is available
            booking.setUserId(userId);
            booking.setStatus(BookingStatus.CONFIRMED);
            return ResponseEntity.ok(bookingService.save(booking));

        } catch (FeignException e) {
            // Catch the FeignException and throw ResponseStatusException with a custom message
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Worker not available or hour not available.");
        }
    }


    @PutMapping("/user/{id}/reschedule")
    public ResponseEntity<?> rescheduleBooking(@RequestHeader("X-User-Id") String userId, @PathVariable Long id, @RequestParam String newTime) {
        // Find the booking by ID
        Booking booking = bookingService.findById(id);
        if (booking == null) {
            // If booking not found, throw a ResponseStatusException
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking with ID " + id + " not found.");
        }

        // Parse the new time
        LocalDateTime parsed = LocalDateTime.parse(newTime);

        try {
            // Check if the new time slot is available (assuming you have some logic for that)
            if (!isTimeSlotAvailable(parsed)) {
                // If the time slot is not available, throw a ResponseStatusException
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Time slot " + newTime + " is not available.");
            }

            // Attempt to reschedule the booking by removing the existing time slot
            dispatcher.reschedule("reschedule", booking, parsed);
            bookingService.save(booking);

            return ResponseEntity.ok("Booking rescheduled successfully");

        } catch (FeignException.NotFound e) {
            // Handle FeignException (404) when worker-service endpoint is not found
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Worker service not found or unable to remove time slot.");
        } catch (IllegalStateException e) {
            // Catch IllegalStateException and throw a ResponseStatusException with a custom message
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Booking cannot be rescheduled.");
        }
    }
    @PutMapping("/user/{id}/cancel")
    public ResponseEntity<?> cancelBooking(@RequestHeader("X-User-Id") String userId, @PathVariable Long id) {
        // Find the booking by ID
        Booking booking = bookingService.findById(id);
        if (booking == null) {
            // If booking not found, throw a ResponseStatusException
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking with ID " + id + " not found.");
        }

        try {
            // Perform the cancel operation (assuming it involves worker service interaction)
            dispatcher.cancel("cancel", booking);

            // Save the updated booking
            bookingService.save(booking);

            return ResponseEntity.ok("Booking cancelled successfully");

        } catch (FeignException.NotFound e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Worker service not found or unable to cancel the time slot.");
        } catch (FeignException.BadRequest e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to cancel the time slot. Invalid data or request.");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "An unexpected error occurred while cancelling the booking.");
        }
    }

    private boolean isTimeSlotAvailable(LocalDateTime newTime) {
        return true; // return true if the slot is available, false if not
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