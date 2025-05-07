package com.example.bookingService.command;


import com.example.bookingService.client.WorkerClient;
import com.example.bookingService.model.Booking;
import com.example.bookingService.model.BookingStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component("reschedule")
public class RescheduleBooking implements BookingCommand {

    private LocalDateTime newTime;

    @Autowired
    private WorkerClient workerClient;

    public void setNewTime(LocalDateTime newTime) {
        this.newTime = newTime;
    }

    @Override
    public void execute(Booking booking) {
        if (booking.getStatus() != BookingStatus.CONFIRMED ||
                booking.getTimeslot().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Booking cannot be rescheduled.");
        }

        int oldHour = booking.getTimeslot().getHour();
        int newHour = newTime.getHour();

        // Add old hour back (increase availability)
        workerClient.addTimeSlot(booking.getWorkerId(), 1);

        // Try removing one slot (simulate new booking)
        String result = workerClient.removeTimeSlot(booking.getWorkerId(), 1);
        if (!result.toLowerCase().contains("success")) {
            throw new IllegalStateException("New hour not available.");
        }

        booking.setTimeslot(newTime);
    }

}

