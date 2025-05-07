package com.algorithmtracker.algorithm;

/**
 * Implementation of various searching algorithms.
 * Includes Linear Search and Binary Search.
 */
public class SearchingAlgorithms {
    
    /**
     * Linear Search implementation.
     */
    public static class LinearSearch implements Algorithm {
        
        /**
         * Searches for a target value in an array using Linear Search.
         * 
         * @param arr The array to search in
         * @param target The value to search for
         * @return The index of the target if found, -1 otherwise
         */
        public int search(int[] arr, int target) {
            for (int i = 0; i < arr.length; i++) {
                if (arr[i] == target) {
                    return i;
                }
            }
            return -1;
        }
        
        @Override
        public String getName() {
            return "Linear Search";
        }
        
        @Override
        public String getDescription() {
            return "A simple search algorithm that checks each element of the list until the target element is found or the list ends.";
        }
        
        @Override
        public String getTimeComplexity() {
            return "O(n)";
        }
        
        @Override
        public String getSpaceComplexity() {
            return "O(1)";
        }
        
        @Override
        public AlgorithmCategory getCategory() {
            return AlgorithmCategory.SEARCHING;
        }
    }
    
    /**
     * Binary Search implementation.
     * Note: The array must be sorted for Binary Search to work correctly.
     */
    public static class BinarySearch implements Algorithm {
        
        /**
         * Searches for a target value in a sorted array using Binary Search.
         * 
         * @param arr The sorted array to search in
         * @param target The value to search for
         * @return The index of the target if found, -1 otherwise
         */
        public int search(int[] arr, int target) {
            int left = 0;
            int right = arr.length - 1;
            
            while (left <= right) {
                int mid = left + (right - left) / 2;
                
                // Check if target is present at mid
                if (arr[mid] == target) {
                    return mid;
                }
                
                // If target is greater, ignore left half
                if (arr[mid] < target) {
                    left = mid + 1;
                }
                // If target is smaller, ignore right half
                else {
                    right = mid - 1;
                }
            }
            
            // Target not found
            return -1;
        }
        
        /**
         * Recursive implementation of Binary Search.
         * 
         * @param arr The sorted array to search in
         * @param target The value to search for
         * @param left The left index
         * @param right The right index
         * @return The index of the target if found, -1 otherwise
         */
        public int searchRecursive(int[] arr, int target, int left, int right) {
            if (left <= right) {
                int mid = left + (right - left) / 2;
                
                // If the element is present at the middle
                if (arr[mid] == target) {
                    return mid;
                }
                
                // If element is smaller than mid, search in the left subarray
                if (arr[mid] > target) {
                    return searchRecursive(arr, target, left, mid - 1);
                }
                
                // Else search in the right subarray
                return searchRecursive(arr, target, mid + 1, right);
            }
            
            // Element not present in array
            return -1;
        }
        
        @Override
        public String getName() {
            return "Binary Search";
        }
        
        @Override
        public String getDescription() {
            return "A search algorithm that finds the position of a target value within a sorted array by repeatedly dividing the search interval in half.";
        }
        
        @Override
        public String getTimeComplexity() {
            return "O(log n)";
        }
        
        @Override
        public String getSpaceComplexity() {
            return "O(1) iterative, O(log n) recursive";
        }
        
        @Override
        public AlgorithmCategory getCategory() {
            return AlgorithmCategory.SEARCHING;
        }
    }
}