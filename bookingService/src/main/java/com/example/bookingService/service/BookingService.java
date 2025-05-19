package com.example.bookingService.service;

import com.example.bookingService.model.Booking;
import com.example.bookingService.repository.BookingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {

    private static final Logger logger = LoggerFactory.getLogger(BookingService.class);


    @Autowired
    private BookingRepository bookingRepository;
    public Booking save(Booking booking) {
        try {
            Booking savedBooking = bookingRepository.save(booking);
            logger.info("Booking created for user: {}", booking.getUserId());
            return savedBooking;
        } catch (Exception e) {
            logger.error("Failed to reserve slot for user: {}", booking.getUserId(), e);
            throw e;
        }
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