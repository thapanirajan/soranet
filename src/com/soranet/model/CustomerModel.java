package com.soranet.model;

import java.util.UUID;

/**
 *
 * @author thapa
 */
/**
 * Represents a customer with their details and subscription information. This
 * class holds information such as the customer ID, name, email, phone number,
 * selected internet plan, speed, and price.
 *
 * The class provides two constructors: 1. A full constructor that initializes
 * all fields, including a custom customer ID. 
 * 2. A constructor that generates a
 * random customer ID and initializes the other fields based on the provided
 * parameters.
 *
 * It includes getter and setter methods for all fields, allowing access and
 * modification of the customer's details.
 *
 * Fields: - customerId (String): Unique identifier for the customer. - name
 * (String): Name of the customer. - email (String): Email address of the
 * customer. - phoneNumber (String): Phone number of the customer. -
 * internetPlan (String): The internet plan selected by the customer. - speed
 * (String): The speed of the internet plan selected. - price (int): The price
 * of the internet plan.
 *
 * Methods: - Getters: For accessing the customer's details. - Setters: For
 * modifying the customer's details.
 */
public class CustomerModel {

    private String customerId;
    private String name;
    private String email;
    private String phoneNumber;
    private String internetPlan;
    private String speed;
    private int price;

    /**
     * Constructor to create a new customer with a specified ID.
     *
     * @param customerId The unique ID of the customer.
     * @param name The name of the customer.
     * @param email The email of the customer.
     * @param phoneNumber The phone number of the customer.
     * @param internetPlan The name of the internet plan the customer is
     * subscribed to.
     * @param speed The internet speed of the customer's plan.
     * @param price The price of the customer's internet plan.
     */
    public CustomerModel(String customerId, String name, String email, String phoneNumber, String internetPlan, String speed, int price) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.internetPlan = internetPlan;
        this.speed = speed;
        this.price = price;
    }

    /**
     * Constructor to create a new customer without a specified ID, generating
     * one automatically.
     *
     * @param name The name of the customer.
     * @param email The email of the customer.
     * @param phoneNumber The phone number of the customer.
     * @param internetPlan The name of the internet plan the customer is
     * subscribed to.
     * @param speed The internet speed of the customer's plan.
     * @param price The price of the customer's internet plan.
     */
    public CustomerModel(String name, String email, String phoneNumber, String internetPlan, String speed, int price) {
        this.customerId = UUID.randomUUID().toString().substring(0, 8);
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.internetPlan = internetPlan;
        this.speed = speed;
        this.price = price;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getInternetPlan() {
        return internetPlan;
    }

    public String getSpeed() {
        return speed;
    }

    public int getPrice() {
        return price;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setInternetPlan(String internetPlan) {
        this.internetPlan = internetPlan;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
