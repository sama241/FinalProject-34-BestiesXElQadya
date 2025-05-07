package com.example.bookingService.repository;

import com.example.bookingService.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    // If Booking.userId is Long:
    List<Booking> findByUserId(String userId);
    List<Booking> findByWorkerId(String workerId);


}
