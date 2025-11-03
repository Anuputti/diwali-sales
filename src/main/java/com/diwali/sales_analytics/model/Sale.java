package com.diwali.sales_analytics.model;

public class Sale {
    private String userId;
    private String customerName;
    private String state;
    private String zone;
    private String gender;
    private double amount;

    public Sale() {}

    public Sale(String userId, String customerName, String state, String zone, String gender, double amount) {
        this.userId = userId;
        this.customerName = customerName;
        this.state = state;
        this.zone = zone;
        this.gender = gender;
        this.amount = amount;
    }

    public String getUserId() {
        return userId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getState() {
        return state;
    }

    public String getZone() {
        return zone;
    }

    public String getGender() {
        return gender;
    }

    public double getAmount() {
        return amount;
    }
}