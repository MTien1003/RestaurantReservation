package com.restaurant.reservationsystem.models;

public class Orders {
    private int   orderId;
    private String productId;
    private String productName;
    private String type;
    private Double price;
    private Integer quantity;

    public Orders(int orderId, String productId, String productName, String type, Double price, Integer quantity) {
        this.orderId = orderId;
        this.productId = productId;
        this.productName = productName;
        this.type = type;
        this.price = price;
        this.quantity = quantity;
    }
    public int getOrderId(){
        return orderId;
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
    public void setName(String name) {
        this.productName = name;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public Double getPrice() {
        return price;
    }
    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }


}