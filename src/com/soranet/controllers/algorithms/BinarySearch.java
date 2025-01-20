package com.soranet.controllers.algorithms;

import com.soranet.model.CustomerModel;
import java.util.List;

/**
 *
 * @author thapa
 */
public class BinarySearch {

    /**
     * Searches for a customer by their name using a binary search algorithm.
     * The method divides the list into halves recursively until the desired
     * customer is found or the search space is empty.
     *
     * @param searchValue The name of the customer to search for.
     * @param customerList The list of customers to search through.
     * @param left The starting index of the list.
     * @param right The ending index of the list.
     * @return The `CustomerModel` if found, otherwise null.
     */
    public CustomerModel searchByName(String searchValue, List<CustomerModel> customerList, int left, int right) {

        // Base Case
        if (right < left) {
            return null;
        }

        // mid value
        int mid = (left + right) / 2;

        // checks whether searchKey lies on mid point
        if (searchValue.compareToIgnoreCase(customerList.get(mid).getName()) == 0) {
            return customerList.get(mid);
        } else if (searchValue.compareToIgnoreCase(customerList.get(mid).getName()) < 0) {
            return searchByName(searchValue, customerList, left, mid - 1);
        } else {
            return searchByName(searchValue, customerList, mid + 1, right);
        }
    }

}
