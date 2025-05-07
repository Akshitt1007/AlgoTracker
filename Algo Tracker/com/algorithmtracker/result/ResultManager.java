package com.algorithmtracker.result;

import com.algorithmtracker.algorithm.Algorithm;
import com.algorithmtracker.performance.PerformanceTracker.PerformanceResult;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages and exports algorithm comparison results.
 */
public class ResultManager {
    private List<PerformanceResult> results;
    private Map<String, List<PerformanceResult>> sessionResults;
    private String currentSession;
    
    /**
     * Constructs a result manager.
     */
    public ResultManager() {
        results = new ArrayList<>();
        sessionResults = new HashMap<>();
        startNewSession();
    }
    
    /**
     * Starts a new session with a timestamp-based name.
     */
    public void startNewSession() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        currentSession = "session_" + now.format(formatter);
        sessionResults.put(currentSession, new ArrayList<>());
    }
    
    /**
     * Adds a performance result to the current session.
     * 
     * @param result The performance result to add
     */
    public void addResult(PerformanceResult result) {
        results.add(result);
        sessionResults.get(currentSession).add(result);
    }
    
    /**
     * Gets all performance results.
     * 
     * @return The list of all performance results
     */
    public List<PerformanceResult> getAllResults() {
        return results;
    }
    
    /**
     * Gets the performance results for the current session.
     * 
     * @return The list of performance results for the current session
     */
    public List<PerformanceResult> getCurrentSessionResults() {
        return sessionResults.get(currentSession);
    }
    
    /**
     * Gets the performance results for a specific session.
     * 
     * @param session The session name
     * @return The list of performance results for the session, or null if the session doesn't exist
     */
    public List<PerformanceResult> getSessionResults(String session) {
        return sessionResults.getOrDefault(session, null);
    }
    
    /**
     * Gets all session names.
     * 
     * @return The list of session names
     */
    public List<String> getSessionNames() {
        return new ArrayList<>(sessionResults.keySet());
    }
    
    /**
     * Exports the current session results to a CSV file.
     * 
     * @param filePath The path to the output file
     * @throws IOException If an I/O error occurs
     */
    public void exportCurrentSessionToCSV(String filePath) throws IOException {
        exportSessionToCSV(currentSession, filePath);
    }
    
    /**
     * Exports a specific session's results to a CSV file.
     * 
     * @param session The session name
     * @param filePath The path to the output file
     * @throws IOException If an I/O error occurs
     */
    public void exportSessionToCSV(String session, String filePath) throws IOException {
        List<PerformanceResult> sessionData = sessionResults.get(session);
        
        if (sessionData == null) {
            throw new IllegalArgumentException("Session not found: " + session);
        }
        
        // Create directory if it doesn't exist
        Files.createDirectories(Paths.get(filePath).getParent());
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Write header
            writer.write("Algorithm,Category,Input Size,Execution Time (ms),Time Complexity,Space Complexity");
            writer.newLine();
            
            // Write data
            for (PerformanceResult result : sessionData) {
                Algorithm algorithm = result.getAlgorithm();
                writer.write(String.format("%s,%s,%d,%d,%s,%s",
                        algorithm.getName(),
                        algorithm.getCategory().getDisplayName(),
                        result.getInputSize(),
                        result.getExecutionTime(),
                        algorithm.getTimeComplexity(),
                        algorithm.getSpaceComplexity()));
                writer.newLine();
            }
        }
    }
    
    /**
     * Exports all results to a CSV file.
     * 
     * @param filePath The path to the output file
     * @throws IOException If an I/O error occurs
     */
    public void exportAllResultsToCSV(String filePath) throws IOException {
        // Create directory if it doesn't exist
        Files.createDirectories(Paths.get(filePath).getParent());
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Write header
            writer.write("Session,Algorithm,Category,Input Size,Execution Time (ms),Time Complexity,Space Complexity");
            writer.newLine();
            
            // Write data for each session
            for (Map.Entry<String, List<PerformanceResult>> entry : sessionResults.entrySet()) {
                String session = entry.getKey();
                List<PerformanceResult> sessionData = entry.getValue();
                
                for (PerformanceResult result : sessionData) {
                    Algorithm algorithm = result.getAlgorithm();
                    writer.write(String.format("%s,%s,%s,%d,%d,%s,%s",
                            session,
                            algorithm.getName(),
                            algorithm.getCategory().getDisplayName(),
                            result.getInputSize(),
                            result.getExecutionTime(),
                            algorithm.getTimeComplexity(),
                            algorithm.getSpaceComplexity()));
                    writer.newLine();
                }
            }
        }
    }
    
    /**
     * Generates a summary of the current session results.
     * 
     * @return A string containing the summary
     */
    public String generateCurrentSessionSummary() {
        return generateSessionSummary(currentSession);
    }
    
    /**
     * Generates a summary of a specific session's results.
     * 
     * @param session The session name
     * @return A string containing the summary
     */
    public String generateSessionSummary(String session) {
        List<PerformanceResult> sessionData = sessionResults.get(session);
        
        if (sessionData == null || sessionData.isEmpty()) {
            return "No data available for session: " + session;
        }
        
        StringBuilder summary = new StringBuilder();
        summary.append("Summary for session: ").append(session).append("\n\n");
        
        // Group by algorithm category
        Map<Algorithm.AlgorithmCategory, List<PerformanceResult>> categoryResults = new HashMap<>();
        
        for (PerformanceResult result : sessionData) {
            Algorithm.AlgorithmCategory category = result.getAlgorithm().getCategory();
            categoryResults.computeIfAbsent(category, k -> new ArrayList<>()).add(result);
        }
        
        // Generate summary for each category
        for (Map.Entry<Algorithm.AlgorithmCategory, List<PerformanceResult>> entry : categoryResults.entrySet()) {
            Algorithm.AlgorithmCategory category = entry.getKey();
            List<PerformanceResult> categoryData = entry.getValue();
            
            summary.append(category.getDisplayName()).append(" Algorithms:\n");
            summary.append("--------------------------------------------------\n");
            
            // Group by algorithm name
            Map<String, List<PerformanceResult>> algorithmResults = new HashMap<>();
            
            for (PerformanceResult result : categoryData) {
                String name = result.getAlgorithm().getName();
                algorithmResults.computeIfAbsent(name, k -> new ArrayList<>()).add(result);
            }
            
            // Generate summary for each algorithm
            for (Map.Entry<String, List<PerformanceResult>> algoEntry : algorithmResults.entrySet()) {
                String name = algoEntry.getKey();
                List<PerformanceResult> algoData = algoEntry.getValue();
                
                summary.append(name).append(":\n");
                
                // Calculate average execution time
                double avgTime = algoData.stream()
                        .mapToLong(PerformanceResult::getExecutionTime)
                        .average()
                        .orElse(0);
                
                summary.append("  Average Execution Time: ").append(String.format("%.2f", avgTime)).append(" ms\n");
                
                // Find min and max execution times
                long minTime = algoData.stream()
                        .mapToLong(PerformanceResult::getExecutionTime)
                        .min()
                        .orElse(0);
                
                long maxTime = algoData.stream()
                        .mapToLong(PerformanceResult::getExecutionTime)
                        .max()
                        .orElse(0);
                
                summary.append("  Min Execution Time: ").append(minTime).append(" ms\n");
                summary.append("  Max Execution Time: ").append(maxTime).append(" ms\n");
                
                // Get complexity information
                Algorithm algorithm = algoData.get(0).getAlgorithm();
                summary.append("  Time Complexity: ").append(algorithm.getTimeComplexity()).append("\n");
                summary.append("  Space Complexity: ").append(algorithm.getSpaceComplexity()).append("\n\n");
            }
            
            summary.append("\n");
        }
        
        return summary.toString();
    }
}