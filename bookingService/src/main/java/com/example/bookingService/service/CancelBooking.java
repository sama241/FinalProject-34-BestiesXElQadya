package com.example.bookingService.service;

import com.example.bookingService.model.Booking;
import com.example.bookingService.model.BookingStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component("cancel")
public class CancelBooking implements BookingCommand {

    @Override
    public void execute(Booking booking) {
        if (booking.getStatus() == BookingStatus.CONFIRMED &&
            booking.getStartTime().isAfter(LocalDateTime.now().plusHours(24))) {
            booking.setStatus(BookingStatus.CANCELLED);
            booking.setUpdatedAt(LocalDateTime.now());
        } else {
            throw new IllegalStateException("Cannot cancel this booking. It may have already started or is too close.");
        }
    }
}

