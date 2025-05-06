package com.example.bookingService.command;

import com.example.bookingService.model.Booking;

public interface BookingCommand {
    void execute(Booking booking);
}
