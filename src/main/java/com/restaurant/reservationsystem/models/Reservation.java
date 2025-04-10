package com.restaurant.reservationsystem.models;

import java.time.LocalDateTime;

public class Reservation {
    private int reservationId;
    private Customer customer;
    private Table table;
    private LocalDateTime reservationTime;
    private LocalDateTime endTime;
    private int numberOfGuests;
    private String status; // "CONFIRMED", "CANCELLED", "COMPLETED"
    private String specialRequests;

    // Constructors
    public Reservation() {}

    public Reservation(Customer customer, Table table,
                       LocalDateTime reservationTime, int numberOfGuests) {
        this.customer = customer;
        this.table = table;
        this.reservationTime = reservationTime;
        this.endTime = reservationTime.plusHours(2); // Default 2 hours
        this.numberOfGuests = numberOfGuests;
        this.status = "CONFIRMED";
    }

    // Getters and Setters
    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public LocalDateTime getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(LocalDateTime reservationTime) {
        this.reservationTime = reservationTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSpecialRequests() {
        return specialRequests;
    }

    public void setSpecialRequests(String specialRequests) {
        this.specialRequests = specialRequests;
    }

    // Business logic methods
    public boolean isActive() {
        return "CONFIRMED".equals(status) &&
                reservationTime.isAfter(LocalDateTime.now());
    }

    @Override
    public String toString() {
        return "Đặt bàn #" + reservationId + " - " + customer.getName() +
                " tại " + table.getTableName() + " lúc " + reservationTime;
    }
}
