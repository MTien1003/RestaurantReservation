package com.restaurant.reservationsystem.models;



import java.util.Objects;

public class Customer {
    private int customerId;
    private String customerName;
    private String phone;
    private String address;

    // Constructors
    public Customer() {}

    public Customer(int customerId, String customerName, String phone, String address) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.phone = phone;
        this.address = address;

    }

    // Getters and Setters
    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }



    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // equals() và hashCode() dựa trên phone (unique)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(phone, customer.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(phone);
    }

    @Override
    public String toString() {
        return customerName + " (" + phone + ")";
    }
}


