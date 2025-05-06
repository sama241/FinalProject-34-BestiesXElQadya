package com.example.bookingService.service;
import com.example.bookingService.service.BookingCommand;
import com.example.bookingService.model.Booking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class BookingDispatcher {

    private final Map<String, BookingCommand> commandMap;

    @Autowired
    public BookingDispatcher(Map<String, BookingCommand> commandMap) {
        this.commandMap = commandMap;
    }

    public void dispatch(String commandKey, Booking booking) {
        BookingCommand command = commandMap.get(commandKey.toLowerCase());

        if (command == null) {
            throw new IllegalArgumentException("No such command: " + commandKey);
        }

        command.execute(booking);
    }
}
