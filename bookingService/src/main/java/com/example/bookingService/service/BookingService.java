package com.example.bookingService.service;

import com.example.bookingService.model.Booking;
import com.example.bookingService.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    // Create a booking
    public Booking save(Booking booking) {
        return bookingRepository.save(booking);
    }

    // Get all bookings
    public List<Booking> findAll() {
        return bookingRepository.findAll();
    }

    // Get booking by ID
    public Booking findById(Long id) {
        return bookingRepository.findById(id).orElseThrow();
    }

    // Update booking
    public Booking update(Long id, Booking updated) {
        Booking existing = findById(id);

        existing.setUserId(updated.getUserId());
        existing.setWorkerId(updated.getWorkerId());
        existing.setTimeslot(updated.getTimeslot());
        existing.setStatus(updated.getStatus());
        existing.setLocation(updated.getLocation());

        return bookingRepository.save(existing);
    }

    // Delete booking
    public void delete(Long id) {
        bookingRepository.deleteById(id);
    }
    public List<Booking> getBookingsByUserId(String userId) {
        return bookingRepository.findByUserId(userId);
    }

    public List<Booking> getBookingsByWorkerId(String workerId) {
        return bookingRepository.findByWorkerId(workerId);
    }


}
