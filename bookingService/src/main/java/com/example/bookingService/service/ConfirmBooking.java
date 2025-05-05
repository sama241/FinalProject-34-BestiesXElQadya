package com.example.bookingService.service;

import com.example.bookingService.model.Booking;
import com.example.bookingService.model.BookingStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component("arrival")
public class ConfirmBooking implements BookingCommand {

    @Override
    public void execute(Booking booking) {
        if (booking.getStatus() == BookingStatus.CONFIRMED &&
                LocalDateTime.now().isAfter(booking.getStartTime())) {
            booking.setStatus(BookingStatus.IN_PROGRESS);
            booking.setUpdatedAt(LocalDateTime.now());
        } else {
            throw new IllegalStateException("Cannot confirm arrival. Booking is not in the correct state or not started yet.");
        }
    }
}
