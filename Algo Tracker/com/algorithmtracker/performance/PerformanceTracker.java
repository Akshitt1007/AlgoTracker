package com.algorithmtracker.performance;

import com.algorithmtracker.algorithm.Algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Tracks and compares the performance of algorithms.
 */
public class PerformanceTracker {
    
    /**
     * Measures the execution time of an algorithm.
     * 
     * @param <T> The input type
     * @param <R> The result type
     * @param algorithm The algorithm to measure
     * @param input The input data
     * @param function The function to execute
     * @return The execution time in milliseconds
     */
    public <T, R> long measureExecutionTime(Algorithm algorithm, T input, Function<T, R> function) {
        // Warm-up run to avoid JVM optimization effects
        function.apply(input);
        
        // Actual measurement
        long startTime = System.nanoTime();
        function.apply(input);
        long endTime = System.nanoTime();
        
        // Convert nanoseconds to milliseconds with proper rounding
        return (endTime - startTime) / 1_000_000;
    }
    
    /**
     * Compares the execution times of multiple algorithms on the same input.
     * 
     * @param <T> The input type
     * @param <R> The result type
     * @param algorithms The algorithms to compare
     * @param input The input data
     * @param functions The functions to execute for each algorithm
     * @return A map of algorithm names to execution times
     */
    public <T, R> Map<String, Long> compareAlgorithms(
            List<Algorithm> algorithms,
            T input,
            List<Function<T, R>> functions) {
        
        if (algorithms.size() != functions.size()) {
            throw new IllegalArgumentException("Number of algorithms must match number of functions");
        }
        
        Map<String, Long> results = new HashMap<>();
        
        for (int i = 0; i < algorithms.size(); i++) {
            Algorithm algorithm = algorithms.get(i);
            Function<T, R> function = functions.get(i);
            
            long executionTime = measureExecutionTime(algorithm, input, function);
            results.put(algorithm.getName(), executionTime);
        }
        
        return results;
    }
    
    /**
     * Represents a performance result.
     */
    public static class PerformanceResult {
        private Algorithm algorithm;
        private long executionTime;
        private int inputSize;
        
        /**
         * Constructs a performance result.
         * 
         * @param algorithm The algorithm
         * @param executionTime The execution time in milliseconds
         * @param inputSize The size of the input
         */
        public PerformanceResult(Algorithm algorithm, long executionTime, int inputSize) {
            this.algorithm = algorithm;
            this.executionTime = executionTime;
            this.inputSize = inputSize;
        }
        
        /**
         * Gets the algorithm.
         * 
         * @return The algorithm
         */
        public Algorithm getAlgorithm() {
            return algorithm;
        }
        
        /**
         * Gets the execution time.
         * 
         * @return The execution time in milliseconds
         */
        public long getExecutionTime() {
            return executionTime;
        }
        
        /**
         * Gets the input size.
         * 
         * @return The input size
         */
        public int getInputSize() {
            return inputSize;
        }
    }
    
    /**
     * Stores performance results for later analysis.
     */
    private List<PerformanceResult> results = new ArrayList<>();
    
    /**
     * Adds a performance result.
     * 
     * @param result The performance result to add
     */
    public void addResult(PerformanceResult result) {
        results.add(result);
    }
    
    /**
     * Gets all performance results.
     * 
     * @return The list of performance results
     */
    public List<PerformanceResult> getResults() {
        return results;
    }
    
    /**
     * Clears all performance results.
     */
    public void clearResults() {
        results.clear();
    }
    
    /**
     * Gets the average execution time for an algorithm across all results.
     * 
     * @param algorithmName The name of the algorithm
     * @return The average execution time in milliseconds, or -1 if no results exist
     */
    public double getAverageExecutionTime(String algorithmName) {
        List<PerformanceResult> algorithmResults = results.stream()
                .filter(r -> r.getAlgorithm().getName().equals(algorithmName))
                .toList();
        
        if (algorithmResults.isEmpty()) {
            return -1;
        }
        
        double sum = algorithmResults.stream()
                .mapToLong(PerformanceResult::getExecutionTime)
                .sum();
        
        return sum / algorithmResults.size();
    }
    
    /**
     * Gets the fastest algorithm for a specific input size.
     * 
     * @param inputSize The input size
     * @return The name of the fastest algorithm, or null if no results exist
     */
    public String getFastestAlgorithm(int inputSize) {
        List<PerformanceResult> sizeResults = results.stream()
                .filter(r -> r.getInputSize() == inputSize)
                .toList();
        
        if (sizeResults.isEmpty()) {
            return null;
        }
        
        PerformanceResult fastest = sizeResults.stream()
                .min((r1, r2) -> Long.compare(r1.getExecutionTime(), r2.getExecutionTime()))
                .orElse(null);
        
        return fastest != null ? fastest.getAlgorithm().getName() : null;
    }
}