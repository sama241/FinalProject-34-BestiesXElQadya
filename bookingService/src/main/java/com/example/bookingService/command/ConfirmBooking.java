package com.example.bookingService.command;

import com.example.bookingService.model.Booking;
import com.example.bookingService.model.BookingStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component("arrival")
public class ConfirmBooking implements BookingCommand {

    @Override
    public void execute(Booking booking) {
        if (booking.getStatus() == BookingStatus.CONFIRMED &&
            LocalDateTime.now().isAfter(booking.getTimeslot())) {
            booking.setStatus(BookingStatus.IN_PROGRESS);
        } else {
            throw new IllegalStateException("Cannot confirm arrival. Booking is not in the correct state or not started yet.");
        }
    }
}
