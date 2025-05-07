package com.example.bookingService.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String workerId;
    private String userId;

    private LocalDateTime timeslot;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    private String location;



    //constructors
    public Booking() {}

    public Booking(String userId, String workerId, LocalDateTime timeslot,
                   BookingStatus status, String location) {
        this.userId = userId;
        this.workerId = workerId;
        this.timeslot=timeslot;
        this.status = status;
        this.location = location;
    }

    public Booking(Long id, String userId, String workerId, LocalDateTime timeslot,
                   BookingStatus status, String location) {
        this.id = id;
        this.userId = userId;
        this.workerId = workerId;
        this.timeslot = timeslot;
        this.status = status;
        this.location = location;
    }


    // Getters and Setters

    public BookingStatus getStatus() {
        return status;
    }

    public Long getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getWorkerId() {
        return workerId;
    }


    public String getLocation() {
        return location;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setWorkerId(String workerId) {
        this.workerId = workerId;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDateTime getTimeslot() {
        return timeslot;
    }
    public void setTimeslot(LocalDateTime timeslot) {
        this.timeslot = timeslot;
    }

}
