package com.example.bookingService.command;

import com.example.bookingService.client.WorkerClient;
import com.example.bookingService.model.Booking;
import com.example.bookingService.model.BookingStatus;
import com.example.bookingService.rabbitmq.BookingProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component("cancel")
public class CancelBooking implements BookingCommand {

    @Autowired
    private WorkerClient workerClient;
    @Autowired
    private BookingProducer bookingProducer;

    @Override
    public void execute(Booking booking) {
        if (booking.getStatus() == BookingStatus.CONFIRMED &&
                booking.getTimeslot().isAfter(LocalDateTime.now().plusHours(2))) {
            int hour = booking.getTimeslot().getHour();

// ✅ Send cancel update via MQ
            bookingProducer.sendBookingStatusToWorker(booking.getWorkerId(), String.valueOf(hour), "canceled");

            booking.setStatus(BookingStatus.CANCELLED);
//
//            int hour = booking.getTimeslot().getHour();
//            String result= workerClient.addTimeSlot(booking.getWorkerId(), hour);
//            booking.setStatus(BookingStatus.CANCELLED);


        } else {
            throw new IllegalStateException("Booking cannot be cancelled.");
        }
    }
}


