package com.restaurant.reservationsystem.models;

public class Order {
    private int id;
    private String orderName;
    private String orderDate;
    private double totalAmount;
    private int quantity;
    private String status;

    public Order(int id, String orderName, String orderDate, double totalAmount, String status) {
        this.id = id;
        this.orderName = orderName;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getOrderName() {
        return orderName;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }


}
