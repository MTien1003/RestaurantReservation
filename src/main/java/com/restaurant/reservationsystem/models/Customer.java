package com.restaurant.reservationsystem.models;



import java.util.Objects;

public class Customer {
    private int customerId;
    private String name;
    private String phone;
    private String email;
    private String address;

    // Constructors
    public Customer() {}

    public Customer(String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    // Getters and Setters
    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
        return name + " (" + phone + ")";
    }
}


