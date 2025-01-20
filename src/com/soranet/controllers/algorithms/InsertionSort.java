package com.soranet.controllers.algorithms;

import com.soranet.model.CustomerModel;
import java.util.ArrayList;
import java.util.List;

public class InsertionSort {

    /**
     * Sorts a list of CustomerModel objects by their price in ascending order.
     *
     * @param customerList The list of CustomerModel objects to be sorted.
     * @return The sorted list of CustomerModel objects.
     * @throws IllegalArgumentException If the input list is null or empty.
     */
    public List<CustomerModel> sortByPrice(List<CustomerModel> customerList) {
        if (customerList == null || customerList.isEmpty()) {
            throw new IllegalArgumentException("customerList cannot be empty");
        }

        // Create a new list to avoid modifying the original list
        List<CustomerModel> sortedList = new ArrayList<>(customerList);

        // Insertion Sort Algorithm
        for (int i = 1; i < sortedList.size(); i++) {
            CustomerModel current = sortedList.get(i); // Current element to insert
            int j = i - 1;

            // Shift elements greater than current to the right
            while (j >= 0 && sortedList.get(j).getPrice() > current.getPrice()) {
                sortedList.set(j + 1, sortedList.get(j)); // Shift element to the right
                j--;
            }

            // Insert the current element in its correct position
            sortedList.set(j + 1, current);
        }

        return sortedList; // Return the sorted list
    }

    /**
     * Sorts a list of CustomerModel objects by their name in ascending order.
     *
     * @param customerList The list of CustomerModel objects to be sorted.
     * @return The sorted list of CustomerModel objects.
     * @throws IllegalArgumentException If the input list is null or empty.
     */
    public List<CustomerModel> sortByName(List<CustomerModel> customerList) {
        if (customerList == null || customerList.isEmpty()) {
            throw new IllegalArgumentException("internetlist cannot be empty");
        }

        // Create a new list to avoid modifying the original list
        List<CustomerModel> sortedList = new ArrayList<>(customerList);

        for (int i = 1; i < sortedList.size(); i++) {
            CustomerModel currentValue = sortedList.get(i);
            int j = i - 1;

            while (j >= 0 && sortedList.get(j).getName().compareTo(currentValue.getName()) > 0) {
                sortedList.set(j + 1, sortedList.get(j));
                j--;
            }
            sortedList.set(j + 1, currentValue);
        }
        return sortedList;
    }

    /**
     * Sorts a list of CustomerModel objects by their ID in ascending order.
     *
     * @param customerList The list of CustomerModel objects to be sorted.
     * @return The sorted list of CustomerModel objects.
     * @throws IllegalArgumentException If the input list is null or empty.
     */
    public List<CustomerModel> sortById(List<CustomerModel> customerList) {
        if (customerList == null || customerList.isEmpty()) {
            throw new IllegalArgumentException("internetlist cannot be empty");
        }

        // Create a new list to avoid modifying the original list
        List<CustomerModel> sortedList = new ArrayList<>(customerList);

        for (int i = 1; i < sortedList.size(); i++) {
            CustomerModel currentValue = sortedList.get(i);
            int j = i - 1;

            while (j >= 0 && sortedList.get(j).getCustomerId().compareTo(currentValue.getCustomerId()) > 0) {
                sortedList.set(j + 1, sortedList.get(j));
                j--;
            }
            sortedList.set(j + 1, currentValue);
        }
        return sortedList;
    }
}
