package com.restaurant.reservationsystem.models;

public class Product {
    private String productId;
    private String productName;
    private String type;
    private float price;
    private String status;
    private int quantity;

    public Product(String productId, String productName, String type, float price, String status, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.type = type;
        this.price = price;
        this.status = status;
        this.quantity = quantity;
    }
    public String getProductId() {
        return productId;
    }
    public void setProductId(String productId) {
        this.productId = productId;
    }
    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public float getPrice() {
        return price;
    }
    public void setPrice(float price) {
        this.price = price;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
