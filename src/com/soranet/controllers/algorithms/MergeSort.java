package com.soranet.controllers.algorithms;

import com.soranet.model.CustomerModel;
import java.util.List;
import java.util.ArrayList;

/**
 * This class implements the Merge Sort algorithm to sort a list of
 * CustomerModel objects. It provides a method to sort customers by their name
 * in ascending order.
 */
public class MergeSort {

    /**
     * Sorts a list of CustomerModel objects by their name using the Merge Sort
     * algorithm.
     *
     * @param customerList The list of CustomerModel objects to be sorted.
     * @return The sorted list of CustomerModel objects.
     * @throws IllegalArgumentException If the input list is null or empty.
     */
    public List<CustomerModel> sortByName(List<CustomerModel> customerList) {
        // Base case: if the list has 1 or 0 elements, it is already sorted
        if (customerList.size() <= 1) {
            return customerList;
        }

        // Find the middle point to divide the list into two halves
        int mid = customerList.size() / 2;

        // Split the list into left and right halves
        List<CustomerModel> left = new ArrayList<>(customerList.subList(0, mid));
        List<CustomerModel> right = new ArrayList<>(customerList.subList(mid, customerList.size()));

        // Recursively sort both halves
        left = sortByName(left);
        right = sortByName(right);

        // Merge the sorted halves
        return merge(left, right);
    }

    /**
     * Merges two sorted lists of CustomerModel objects into one sorted list.
     *
     * @param left The first sorted list of CustomerModel objects.
     * @param right The second sorted list of CustomerModel objects.
     * @return A merged list of CustomerModel objects in sorted order.
     */
    private List<CustomerModel> merge(List<CustomerModel> left, List<CustomerModel> right) {
        List<CustomerModel> mergedList = new ArrayList<>();
        int leftIndex = 0, rightIndex = 0;

        // Compare elements from both lists and add the smaller one to the merged list
        while (leftIndex < left.size() && rightIndex < right.size()) {
            if (left.get(leftIndex).getName().compareToIgnoreCase(right.get(rightIndex).getName()) <= 0) {
                mergedList.add(left.get(leftIndex));
                leftIndex++;
            } else {
                mergedList.add(right.get(rightIndex));
                rightIndex++;
            }
        }

        // Add any remaining elements from the left list
        while (leftIndex < left.size()) {
            mergedList.add(left.get(leftIndex));
            leftIndex++;
        }

        // Add any remaining elements from the right list
        while (rightIndex < right.size()) {
            mergedList.add(right.get(rightIndex));
            rightIndex++;
        }

        return mergedList;
    }
}
