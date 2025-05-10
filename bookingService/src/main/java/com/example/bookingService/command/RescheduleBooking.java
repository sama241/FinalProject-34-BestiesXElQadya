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

        // Add old hour back (increase availability)
        int hour = booking.getTimeslot().getHour();
        workerClient.addTimeSlot(booking.getWorkerId(), hour);

        // Try removing one slot (simulate new booking)
        int hour2 = newTime.getHour();
        String result = workerClient.removeTimeSlot(booking.getWorkerId(), hour2);
        if (!result.toLowerCase().contains("success")) {
            throw new IllegalStateException("New hour not available.");
        }

        booking.setTimeslot(newTime);
    }

}

