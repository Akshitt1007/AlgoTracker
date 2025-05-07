package com.algorithmtracker.algorithm;

import java.util.Arrays;

/**
 * Implementation of various sorting algorithms.
 * Includes Bubble Sort, Insertion Sort, Selection Sort, Merge Sort, and Quick Sort.
 */
public class SortingAlgorithms {
    
    /**
     * Bubble Sort implementation.
     */
    public static class BubbleSort implements Algorithm {
        
        /**
         * Sorts an array using Bubble Sort algorithm.
         * 
         * @param arr The array to be sorted
         * @return The sorted array
         */
        public int[] sort(int[] arr) {
            int[] result = Arrays.copyOf(arr, arr.length);
            int n = result.length;
            
            for (int i = 0; i < n - 1; i++) {
                for (int j = 0; j < n - i - 1; j++) {
                    if (result[j] > result[j + 1]) {
                        // Swap elements
                        int temp = result[j];
                        result[j] = result[j + 1];
                        result[j + 1] = temp;
                    }
                }
            }
            
            return result;
        }
        
        @Override
        public String getName() {
            return "Bubble Sort";
        }
        
        @Override
        public String getDescription() {
            return "A simple sorting algorithm that repeatedly steps through the list, compares adjacent elements, and swaps them if they are in the wrong order.";
        }
        
        @Override
        public String getTimeComplexity() {
            return "O(n²)";
        }
        
        @Override
        public String getSpaceComplexity() {
            return "O(1)";
        }
        
        @Override
        public AlgorithmCategory getCategory() {
            return AlgorithmCategory.SORTING;
        }
    }
    
    /**
     * Insertion Sort implementation.
     */
    public static class InsertionSort implements Algorithm {
        
        /**
         * Sorts an array using Insertion Sort algorithm.
         * 
         * @param arr The array to be sorted
         * @return The sorted array
         */
        public int[] sort(int[] arr) {
            int[] result = Arrays.copyOf(arr, arr.length);
            int n = result.length;
            
            for (int i = 1; i < n; i++) {
                int key = result[i];
                int j = i - 1;
                
                // Move elements greater than key to one position ahead
                while (j >= 0 && result[j] > key) {
                    result[j + 1] = result[j];
                    j = j - 1;
                }
                result[j + 1] = key;
            }
            
            return result;
        }
        
        @Override
        public String getName() {
            return "Insertion Sort";
        }
        
        @Override
        public String getDescription() {
            return "Builds the sorted array one item at a time by comparing each item with the items before it and inserting it into its correct position.";
        }
        
        @Override
        public String getTimeComplexity() {
            return "O(n²)";
        }
        
        @Override
        public String getSpaceComplexity() {
            return "O(1)";
        }
        
        @Override
        public AlgorithmCategory getCategory() {
            return AlgorithmCategory.SORTING;
        }
    }
    
    /**
     * Selection Sort implementation.
     */
    public static class SelectionSort implements Algorithm {
        
        /**
         * Sorts an array using Selection Sort algorithm.
         * 
         * @param arr The array to be sorted
         * @return The sorted array
         */
        public int[] sort(int[] arr) {
            int[] result = Arrays.copyOf(arr, arr.length);
            int n = result.length;
            
            for (int i = 0; i < n - 1; i++) {
                // Find the minimum element in unsorted array
                int minIdx = i;
                for (int j = i + 1; j < n; j++) {
                    if (result[j] < result[minIdx]) {
                        minIdx = j;
                    }
                }
                
                // Swap the found minimum element with the first element
                int temp = result[minIdx];
                result[minIdx] = result[i];
                result[i] = temp;
            }
            
            return result;
        }
        
        @Override
        public String getName() {
            return "Selection Sort";
        }
        
        @Override
        public String getDescription() {
            return "Divides the input list into two parts: a sorted sublist and an unsorted sublist. Repeatedly selects the smallest element from the unsorted sublist and moves it to the end of the sorted sublist.";
        }
        
        @Override
        public String getTimeComplexity() {
            return "O(n²)";
        }
        
        @Override
        public String getSpaceComplexity() {
            return "O(1)";
        }
        
        @Override
        public AlgorithmCategory getCategory() {
            return AlgorithmCategory.SORTING;
        }
    }
    
    /**
     * Merge Sort implementation.
     */
    public static class MergeSort implements Algorithm {
        
        /**
         * Sorts an array using Merge Sort algorithm.
         * 
         * @param arr The array to be sorted
         * @return The sorted array
         */
        public int[] sort(int[] arr) {
            int[] result = Arrays.copyOf(arr, arr.length);
            mergeSort(result, 0, result.length - 1);
            return result;
        }
        
        /**
         * Recursive method to divide and merge the array.
         * 
         * @param arr The array to be sorted
         * @param left The left index
         * @param right The right index
         */
        private void mergeSort(int[] arr, int left, int right) {
            if (left < right) {
                // Find the middle point
                int mid = left + (right - left) / 2;
                
                // Sort first and second halves
                mergeSort(arr, left, mid);
                mergeSort(arr, mid + 1, right);
                
                // Merge the sorted halves
                merge(arr, left, mid, right);
            }
        }
        
        /**
         * Merges two subarrays of arr[].
         * 
         * @param arr The array to be merged
         * @param left The left index
         * @param mid The middle index
         * @param right The right index
         */
        private void merge(int[] arr, int left, int mid, int right) {
            // Find sizes of two subarrays to be merged
            int n1 = mid - left + 1;
            int n2 = right - mid;
            
            // Create temp arrays
            int[] L = new int[n1];
            int[] R = new int[n2];
            
            // Copy data to temp arrays
            for (int i = 0; i < n1; ++i) {
                L[i] = arr[left + i];
            }
            for (int j = 0; j < n2; ++j) {
                R[j] = arr[mid + 1 + j];
            }
            
            // Merge the temp arrays
            int i = 0, j = 0;
            int k = left;
            while (i < n1 && j < n2) {
                if (L[i] <= R[j]) {
                    arr[k] = L[i];
                    i++;
                } else {
                    arr[k] = R[j];
                    j++;
                }
                k++;
            }
            
            // Copy remaining elements of L[] if any
            while (i < n1) {
                arr[k] = L[i];
                i++;
                k++;
            }
            
            // Copy remaining elements of R[] if any
            while (j < n2) {
                arr[k] = R[j];
                j++;
                k++;
            }
        }
        
        @Override
        public String getName() {
            return "Merge Sort";
        }
        
        @Override
        public String getDescription() {
            return "A divide and conquer algorithm that divides the input array into two halves, recursively sorts them, and then merges the sorted halves.";
        }
        
        @Override
        public String getTimeComplexity() {
            return "O(n log n)";
        }
        
        @Override
        public String getSpaceComplexity() {
            return "O(n)";
        }
        
        @Override
        public AlgorithmCategory getCategory() {
            return AlgorithmCategory.SORTING;
        }
    }
    
    /**
     * Quick Sort implementation.
     */
    public static class QuickSort implements Algorithm {
        
        /**
         * Sorts an array using Quick Sort algorithm.
         * 
         * @param arr The array to be sorted
         * @return The sorted array
         */
        public int[] sort(int[] arr) {
            int[] result = Arrays.copyOf(arr, arr.length);
            quickSort(result, 0, result.length - 1);
            return result;
        }
        
        /**
         * Recursive method to implement Quick Sort.
         * 
         * @param arr The array to be sorted
         * @param low The starting index
         * @param high The ending index
         */
        private void quickSort(int[] arr, int low, int high) {
            if (low < high) {
                // Partition the array and get the pivot index
                int pivotIndex = partition(arr, low, high);
                
                // Recursively sort elements before and after partition
                quickSort(arr, low, pivotIndex - 1);
                quickSort(arr, pivotIndex + 1, high);
            }
        }
        
        /**
         * Partitions the array and returns the pivot index.
         * 
         * @param arr The array to be partitioned
         * @param low The starting index
         * @param high The ending index
         * @return The pivot index
         */
        private int partition(int[] arr, int low, int high) {
            // Select the rightmost element as pivot
            int pivot = arr[high];
            
            // Index of smaller element
            int i = (low - 1);
            
            for (int j = low; j < high; j++) {
                // If current element is smaller than or equal to pivot
                if (arr[j] <= pivot) {
                    i++;
                    
                    // Swap arr[i] and arr[j]
                    int temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
                }
            }
            
            // Swap arr[i+1] and arr[high] (or pivot)
            int temp = arr[i + 1];
            arr[i + 1] = arr[high];
            arr[high] = temp;
            
            return i + 1;
        }
        
        @Override
        public String getName() {
            return "Quick Sort";
        }
        
        @Override
        public String getDescription() {
            return "A divide and conquer algorithm that picks an element as pivot and partitions the array around the pivot.";
        }
        
        @Override
        public String getTimeComplexity() {
            return "O(n log n) average, O(n²) worst case";
        }
        
        @Override
        public String getSpaceComplexity() {
            return "O(log n)";
        }
        
        @Override
        public AlgorithmCategory getCategory() {
            return AlgorithmCategory.SORTING;
        }
    }
}