package com.example.bookingService.command;

import com.example.bookingService.model.Booking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class BookingDispatcher {

    private final Map<String, BookingCommand> commandMap;
    private final ApplicationContext context;

    @Autowired
    public BookingDispatcher(Map<String, BookingCommand> commandMap, ApplicationContext context) {
        this.commandMap = commandMap;
        this.context = context;
    }

    // ✅ Standard commands (cancel, arrival)
    public void cancel(String commandKey, Booking booking) {
        BookingCommand command = commandMap.get(commandKey.toLowerCase());

        if (command == null) {
            throw new IllegalArgumentException("No such command: " + commandKey);
        }

        command.execute(booking);
    }

    // ✅ Extended support for reschedule
    public void reschedule(String commandKey, Booking booking, LocalDateTime newTime) {
        BookingCommand command;

        if ("reschedule".equalsIgnoreCase(commandKey)) {
            RescheduleBooking reschedule = context.getBean(RescheduleBooking.class);
            reschedule.setNewTime(newTime);
            command = reschedule;
        } else {
            command = commandMap.get(commandKey.toLowerCase());
        }

        if (command == null) {
            throw new IllegalArgumentException("No such command: " + commandKey);
        }

        command.execute(booking);
    }
}
