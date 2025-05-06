package com.example.bookingService.service;


import com.example.bookingService.model.Booking;
import com.example.bookingService.model.BookingStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component("reschedule")
public class RescheduleBooking implements BookingCommand {

    private LocalDateTime newStart;
    private LocalDateTime newEnd;

    // Use setter injection or constructor injection in real code
    public void setNewTimes(LocalDateTime newStart, LocalDateTime newEnd) {
        this.newStart = newStart;
        this.newEnd = newEnd;
    }

    @Override
    public void execute(Booking booking) {
        if (booking.getStatus() == BookingStatus.CONFIRMED &&
                booking.getStartTime().isAfter(LocalDateTime.now())) {
            booking.setStartTime(newStart);
            booking.setEndTime(newEnd);
            booking.setUpdatedAt(LocalDateTime.now());
        } else {
            throw new IllegalStateException("Cannot reschedule a booking that has started or is not confirmed.");
        }
    }
}
