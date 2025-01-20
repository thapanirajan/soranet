package com.soranet.controllers;

import com.soranet.model.CustomerModel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CustomerController {

    private final List<CustomerModel> customerList;

    public CustomerController() {
        customerList = new ArrayList<>();
        initializeCustomers();
        returnEarnings();
    }

    /**
     * Adds a new customer to the customer list if no duplicate name exists. The
     * method checks whether a customer with the same name already exists and,
     * if not, adds the new customer to the list.
     *
     * @param name The name of the customer to be added.
     * @param email The email address of the customer.
     * @param phoneNumber The phone number of the customer.
     * @param internetPlan The internet plan subscribed to by the customer.
     * @param speed The internet speed of the customer's plan.
     * @param price The price of the customer's internet plan.
     * @return true  if the customer was successfully added,
     *  false if a customer with the same name already exists.
     */
    public boolean addCustomer(String name, String email, String phoneNumber, String internetPlan, String speed, int price) {
        // Check if a customer with the same name already exists
        for (CustomerModel customer : customerList) {
            if (customer.getName().equalsIgnoreCase(name)) {
                return false; // Username already exists
            }
        }
        // If no duplicate found, add the new customer
        CustomerModel newCustomer = new CustomerModel(name, email, phoneNumber, internetPlan, speed, price);
        customerList.add(newCustomer);
        return true;
    }
    
    
    
    /**
     * Updates the details of an existing customer identified by their customer
     * ID. The method searches for a customer with the specified ID in the
     * customer list and updates their name, phone number, internet plan, speed,
     * and price.
     *
     * @param customerId The unique ID of the customer to be updated.
     * @param name The new name of the customer.
     * @param phoneNumber The new phone number of the customer.
     * @param internetPlan The new internet plan of the customer.
     * @param speed The new internet speed of the customer's plan.
     * @param price The new price of the customer's internet plan.
     * @return true if the customer was successfully updated, false if no
     * customer with the specified ID was found.
     */
    public boolean updateCustomer(String customerId, String name, String phoneNumber, String internetPlan, String speed, int price) {
        for (CustomerModel customer : customerList) {
            if (customer.getCustomerId().equals(customerId)) {
                customer.setName(name);
                customer.setPhoneNumber(phoneNumber);
                customer.setInternetPlan(internetPlan);
                customer.setSpeed(speed);
                customer.setPrice(price);
                return true;
            }
        }
        return false;
    }

    
    
    
    /**
     * Retrieves the list of all customers. This method returns the current list
     * of customers stored in the system.
     *
     * @return A list of all {@code CustomerModel} objects representing the
     * customers.
     */
    public List<CustomerModel> getAllCustomers() {
        return customerList;
    }

    
    
    
    /**
     * Finds a customer by their unique customer ID. This method searches
     * through the list of customers and returns the customer that matches the
     * provided customer ID. If no such customer is found, it returns null.
     *
     * @param customerId The unique ID of the customer to be searched.
     * @return The CustomerModel object representing the customer, or null if no
     * customer is found.
     */
    public CustomerModel getCustomerById(String customerId) {
        for (CustomerModel customer : customerList) {
            if (customer.getCustomerId().equals(customerId)) {
                return customer;
            }
        }
        return null;
    }

    
    
    
    /**
     * Returns the number of customers in the customer list. This method
     * provides the current size of the list, indicating how many customers are
     * stored.
     *
     * @return The size of the customer list, representing the total number of
     * customers.
     */
    public int returnSize() {
        return customerList.size();
    }

    
    
    
    /**
     * Deletes a customer from the customer list based on their unique customer
     * ID. This method searches for a customer with the specified ID and removes
     * them from the list.
     *
     * @param customerId The unique ID of the customer to be deleted.
     * @return true if the customer was successfully deleted, false if no
     * customer with the specified ID was found.
     */
    public boolean deleteCustomerById(String customerId) {
        return customerList.removeIf(customer -> customer.getCustomerId().equals(customerId));
    }

    
    
    
    /**
     * Calculates the total earnings from all customers by summing up their
     * respective prices. This method iterates through the list of all customers
     * and adds up the price of each customer's internet plan.
     *
     * @return The total earnings, calculated as the sum of the prices of all
     * customer's plans.
     */
    public final int returnEarnings() {
        List<CustomerModel> customers = getAllCustomers();
        int totalIncome = 0;
        for (CustomerModel customList : customers) {
            totalIncome = totalIncome + customList.getPrice();
        }
        return totalIncome;
    }

    
    
    
    private void initializeCustomers() {
        // Adding some dummy customer data
        customerList.add(new CustomerModel(UUID.randomUUID().toString().substring(0, 8), "Alice Smith", "alice.smith@example.com", "9876543210", "Standard Plan", "150 Mbps", 1500));
        customerList.add(new CustomerModel(UUID.randomUUID().toString().substring(0, 8), "Michael Johnson", "michael.j@example.com", "9123456789", "Basic Plan", "50 Mbps", 1000));
        customerList.add(new CustomerModel(UUID.randomUUID().toString().substring(0, 8), "Sophia Brown", "sophia.b@example.com", "9988776655", "Premium Plan", "250 Mbps", 2500));
        customerList.add(new CustomerModel(UUID.randomUUID().toString().substring(0, 8), "Liam Wilson", "liam.w@example.com", "9876501234", "Ultra Plan", "500 Mbps", 5000));
        customerList.add(new CustomerModel(UUID.randomUUID().toString().substring(0, 8), "Olivia Taylor", "olivia.t@example.com", "9876544321", "Standard Plan", "150 Mbps", 1500));
        customerList.add(new CustomerModel(UUID.randomUUID().toString().substring(0, 8), "Noah Anderson", "noah.a@example.com", "9900112233", "Basic Plan", "50 Mbps", 1000));
        customerList.add(new CustomerModel(UUID.randomUUID().toString().substring(0, 8), "Isabella Martinez", "isabella.m@example.com", "9911223344", "Premium Plan", "250 Mbps", 2500));
        customerList.add(new CustomerModel(UUID.randomUUID().toString().substring(0, 8), "James Thomas", "james.t@example.com", "9922334455", "Ultra Plan", "500 Mbps", 5000));
        customerList.add(new CustomerModel(UUID.randomUUID().toString().substring(0, 8), "Amelia Hernandez", "amelia.h@example.com", "9800112233", "Standard Plan", "150 Mbps", 1500));
        customerList.add(new CustomerModel(UUID.randomUUID().toString().substring(0, 8), "Ethan Garcia", "ethan.g@example.com", "9811223344", "Basic Plan", "50 Mbps", 1000));
        customerList.add(new CustomerModel(UUID.randomUUID().toString().substring(0, 8), "Mia Davis", "mia.d@example.com", "9822334455", "Premium Plan", "250 Mbps", 2500));
        customerList.add(new CustomerModel(UUID.randomUUID().toString().substring(0, 8), "Lucas Robinson", "lucas.r@example.com", "9833445566", "Ultra Plan", "500 Mbps", 5000));
        customerList.add(new CustomerModel(UUID.randomUUID().toString().substring(0, 8), "Charlotte Clark", "charlotte.c@example.com", "9844556677", "Standard Plan", "150 Mbps", 1500));
        customerList.add(new CustomerModel(UUID.randomUUID().toString().substring(0, 8), "Elijah Rodriguez", "elijah.r@example.com", "9855667788", "Basic Plan", "50 Mbps", 1000));
        customerList.add(new CustomerModel(UUID.randomUUID().toString().substring(0, 8), "Harper Lewis", "harper.l@example.com", "9866778899", "Premium Plan", "250 Mbps", 2500));
    }
}
