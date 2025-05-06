package com.example.bookingService.service;

import com.example.bookingService.model.Booking;

public interface BookingCommand {
    void execute(Booking booking);
}
