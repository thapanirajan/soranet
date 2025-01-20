package com.soranet.model;

/**
 *
 * @author thapa
 */
/**
 * Represents an internet plan with its associated details such as name, speed,
 * and price. This class provides the structure to store and manipulate data
 * related to internet plans available to customers.
 *
 * Fields: - name (String): The name of the internet plan. - speed (String): The
 * speed of the internet plan (e.g., "100 Mbps"). - price (int): The price of
 * the internet plan.
 *
 * Methods: - Getters: For accessing the internet plan's name, speed, and price.
 * - Setters: For modifying the internet plan's name, speed, and price.
 */
public class InternetModel {

    private String name;
    private String speed;
    private int price;

    /**
     * Constructor to initialize an internet plan with the given details.
     *
     * @param name The name of the internet plan.
     * @param speed The speed of the internet plan.
     * @param price The price of the internet plan.
     */
    public InternetModel(String name, String speed, int price) {
        this.name = name;
        this.speed = speed;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getSpeed() {
        return speed;
    }

    public int getPrice() {
        return price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public void setPrice(int price) {
        this.price = price;
    }

}
