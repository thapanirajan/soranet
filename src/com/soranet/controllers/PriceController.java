package com.soranet.controllers;

import com.soranet.model.InternetModel;
import java.util.ArrayList;
import java.util.List;

/**
 * This class manages the internet plans, including adding, updating, deleting
 * plans, calculating the total income from all plans, and displaying available
 * plans. It provides methods to manipulate and access the list of internet
 * plans.
 */
public class PriceController {

    private final List<InternetModel> internetList;

    public PriceController() {
        internetList = new ArrayList<>();
        getTotalIncome();
        displayPlans();
    }

    /**
     * Adds a new internet plan to the list if no plan with the same name
     * exists.
     *
     * @param name The name of the internet plan to be added.
     * @param speed The speed of the internet plan.
     * @param price The price of the internet plan.
     * @return true if the plan was successfully added, false if a plan with the
     * same name already exists.
     */
    public boolean addPlan(String name, String speed, int price) {
        for (InternetModel plans : internetList) {
            if (name.equals(plans.getName())) {
                return false;
            }
        }
        InternetModel newPlan = new InternetModel(name, speed, price);
        internetList.add(newPlan);
        return true;
    }

    /**
     * Updates an existing internet plan with a new speed and price.
     *
     * @param name The name of the plan to be updated.
     * @param speed The new speed of the internet plan.
     * @param price The new price of the internet plan.
     * @return true if the plan was successfully updated, false if no plan with
     * the specified name exists.
     */
    public boolean updatePlan(String name, String speed, int price) {
        for (InternetModel plan : internetList) {
            if (plan.getName().equals(name)) {
                plan.setName(name);
                plan.setPrice(price);
                plan.setSpeed(speed);
                return true;
            }
        }
        return false;
    }

    /**
     * Deletes an internet plan from the list based on its name.
     *
     * @param name The name of the plan to be deleted.
     * @return true if the plan was successfully deleted, false if no plan with
     * the specified name exists.
     */
    public boolean deletePlan(String name) {
        return internetList.removeIf(price -> price.getName().equals(name));
    }

    /**
     * Returns the total number of internet plans.
     *
     * @return The size of the internet plan list.
     */
    public int planSize() {
        return internetList.size();
    }

    /**
     * Retrieves the list of all internet plans.
     *
     * @return A list of all InternetModel objects representing the plans.
     */
    public List<InternetModel> getAllPlan() {
        return internetList;
    }

    /**
     * Calculates and prints the total income by summing up the price of all
     * internet plans.
     *
     * @return The total income generated from all internet plans.
     */
    public final int getTotalIncome() {
        List<InternetModel> PricingList = getAllPlan();
        int totalIncome = 0;
        for (InternetModel price : PricingList) {
            totalIncome = totalIncome + price.getPrice();
        }
        System.out.println(totalIncome);
        return totalIncome;
    }

    /**
     * Adds some predefined internet plans to the list.
     */
    public final void displayPlans() {
        addPlan("Basic Plan", "50 Mbps", 500);
        addPlan("Standard Plan", "150 Mbps", 1500);
        addPlan("Family Plan", "200 Mbps", 2000);
        addPlan("Premium Plan", "250 Mbps", 2500);
        addPlan("Ultra Plan", "500 Mbps", 5000);
    }
}
