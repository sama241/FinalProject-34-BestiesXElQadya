package com.example.bookingService.command;

import com.example.bookingService.client.WorkerClient;
import com.example.bookingService.model.Booking;
import com.example.bookingService.model.BookingStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component("cancel")
public class CancelBooking implements BookingCommand {

    @Autowired
    private WorkerClient workerClient;

    @Override
    public void execute(Booking booking) {
        if (booking.getStatus() == BookingStatus.CONFIRMED &&
                booking.getTimeslot().isAfter(LocalDateTime.now().plusHours(2))) {

            int hour = booking.getTimeslot().getHour();
            workerClient.addTimeSlots(booking.getWorkerId().toString(), hour);

            booking.setStatus(BookingStatus.CANCELLED);
        } else {
            throw new IllegalStateException("Booking cannot be cancelled.");
        }
    }
}


