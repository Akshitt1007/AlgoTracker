package com.algorithmtracker.data;

import com.algorithmtracker.algorithm.GraphAlgorithms.Graph;

import java.util.Arrays;
import java.util.Random;

/**
 * Generates test data for algorithm testing.
 */
public class TestDataGenerator {
    private Random random;
    
    /**
     * Constructs a test data generator with a random seed.
     */
    public TestDataGenerator() {
        this.random = new Random();
    }
    
    /**
     * Constructs a test data generator with a specified seed.
     * 
     * @param seed The seed for the random number generator
     */
    public TestDataGenerator(long seed) {
        this.random = new Random(seed);
    }
    
    /**
     * Generates a random integer array of the specified size.
     * 
     * @param size The size of the array
     * @param min The minimum value (inclusive)
     * @param max The maximum value (exclusive)
     * @return The generated array
     */
    public int[] generateRandomIntArray(int size, int min, int max) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(max - min) + min;
        }
        return array;
    }
    
    /**
     * Generates a sorted integer array of the specified size.
     * 
     * @param size The size of the array
     * @param min The minimum value (inclusive)
     * @param max The maximum value (exclusive)
     * @return The generated sorted array
     */
    public int[] generateSortedIntArray(int size, int min, int max) {
        int[] array = generateRandomIntArray(size, min, max);
        Arrays.sort(array);
        return array;
    }
    
    /**
     * Generates a nearly sorted integer array of the specified size.
     * 
     * @param size The size of the array
     * @param min The minimum value (inclusive)
     * @param max The maximum value (exclusive)
     * @param swapFactor The percentage of elements to swap (0.0 to 1.0)
     * @return The generated nearly sorted array
     */
    public int[] generateNearlySortedIntArray(int size, int min, int max, double swapFactor) {
        int[] array = generateSortedIntArray(size, min, max);
        int swaps = (int) (size * swapFactor);
        
        for (int i = 0; i < swaps; i++) {
            int idx1 = random.nextInt(size);
            int idx2 = random.nextInt(size);
            
            // Swap elements
            int temp = array[idx1];
            array[idx1] = array[idx2];
            array[idx2] = temp;
        }
        
        return array;
    }
    
    /**
     * Generates a random string of the specified length.
     * 
     * @param length The length of the string
     * @return The generated string
     */
    public String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            char c = (char) (random.nextInt(26) + 'a');
            sb.append(c);
        }
        return sb.toString();
    }
    
    /**
     * Generates a random graph with the specified number of vertices and edges.
     * 
     * @param vertices The number of vertices
     * @param edges The number of edges
     * @param maxWeight The maximum weight of an edge
     * @return The generated graph
     */
    public Graph generateRandomGraph(int vertices, int edges, int maxWeight) {
        Graph graph = new Graph(vertices);
        
        // Ensure the graph is connected
        for (int i = 0; i < vertices - 1; i++) {
            int weight = random.nextInt(maxWeight) + 1;
            graph.addEdge(i, i + 1, weight);
        }
        
        // Add remaining random edges
        int remainingEdges = edges - (vertices - 1);
        for (int i = 0; i < remainingEdges; i++) {
            int source = random.nextInt(vertices);
            int destination = random.nextInt(vertices);
            
            // Avoid self-loops
            if (source != destination) {
                int weight = random.nextInt(maxWeight) + 1;
                graph.addEdge(source, destination, weight);
            }
        }
        
        return graph;
    }
    
    /**
     * Generates random weights and values for the Knapsack problem.
     * 
     * @param items The number of items
     * @param maxWeight The maximum weight of an item
     * @param maxValue The maximum value of an item
     * @return An array where the first element is the weights array and the second element is the values array
     */
    public int[][] generateKnapsackData(int items, int maxWeight, int maxValue) {
        int[] weights = new int[items];
        int[] values = new int[items];
        
        for (int i = 0; i < items; i++) {
            weights[i] = random.nextInt(maxWeight) + 1;
            values[i] = random.nextInt(maxValue) + 1;
        }
        
        return new int[][] { weights, values };
    }
    
    /**
     * Generates a reversed integer array of the specified size.
     * 
     * @param size The size of the array
     * @param min The minimum value (inclusive)
     * @param max The maximum value (exclusive)
     * @return The generated reversed array
     */
    public int[] generateReversedIntArray(int size, int min, int max) {
        int[] array = generateSortedIntArray(size, min, max);
        
        // Reverse the array
        for (int i = 0; i < size / 2; i++) {
            int temp = array[i];
            array[i] = array[size - i - 1];
            array[size - i - 1] = temp;
        }
        
        return array;
    }
    
    /**
     * Generates an array with many duplicates.
     * 
     * @param size The size of the array
     * @param uniqueValues The number of unique values
     * @return The generated array with duplicates
     */
    public int[] generateArrayWithDuplicates(int size, int uniqueValues) {
        int[] array = new int[size];
        
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(uniqueValues);
        }
        
        return array;
    }
}