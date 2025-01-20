package com.soranet.controllers.algorithms;

import com.soranet.model.InternetModel;
import java.util.ArrayList;
import java.util.List;

/**
 * This class implements the Selection Sort algorithm to sort a list of
 * InternetModel objects. It provides a method to sort internet plans by their
 * price in either ascending or descending order.
 */
public class SelectionSort {

    List<InternetModel> internetSortList;

    /**
     * Constructor to initialize the list for sorting.
     */
    public SelectionSort() {
        internetSortList = new ArrayList<>();
    }

    /**
     * Sorts a list of InternetModel objects by their price using the Selection
     * Sort algorithm. The sort order can be either ascending or descending
     * based on the provided flag.
     *
     * @param internetList The list of InternetModel objects to be sorted.
     * @param isDesc A Boolean flag indicating if the sort should be descending
     * (true) or ascending (false).
     * @return The sorted list of InternetModel objects.
     * @throws IllegalArgumentException If the input list is null or empty.
     */
    public List<InternetModel> sortByPrice(List<InternetModel> internetList, boolean isDesc) {
        internetSortList.clear();
        internetSortList.addAll(internetList);

        if (internetSortList == null || internetSortList.isEmpty()) {
            throw new IllegalArgumentException("internetlist cannot be empty");
        }

        // Selection Sort Algorithm: Find the minimum/maximum element in each iteration and swap
        for (int i = 0; i < internetSortList.size() - 1; i++) {
            int minIndex = i;

            // Find the minimum or maximum based on the isDesc flag
            for (int j = i + 1; j < internetSortList.size(); j++) {
                if (isDesc) {
                    if (internetSortList.get(j).getPrice() > internetSortList.get(minIndex).getPrice()) {
                        minIndex = j;
                    }
                } else {
                    if (internetSortList.get(j).getPrice() < internetSortList.get(minIndex).getPrice()) {
                        minIndex = j;
                    }
                }
            }

            // Swap the elements at minIndex and i
            InternetModel temp = internetSortList.get(minIndex);
            internetSortList.set(minIndex, internetSortList.get(i));
            internetSortList.set(i, temp);
        }
        return internetSortList;
    }
}
