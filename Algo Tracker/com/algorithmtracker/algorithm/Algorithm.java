package com.algorithmtracker.algorithm;

/**
 * Base interface for all algorithms in the system.
 * Defines common methods that all algorithm implementations must provide.
 */
public interface Algorithm {
    
    /**
     * Gets the name of the algorithm.
     * 
     * @return The algorithm name
     */
    String getName();
    
    /**
     * Gets a brief description of the algorithm.
     * 
     * @return The algorithm description
     */
    String getDescription();
    
    /**
     * Gets the time complexity of the algorithm in Big O notation.
     * 
     * @return The time complexity
     */
    String getTimeComplexity();
    
    /**
     * Gets the space complexity of the algorithm in Big O notation.
     * 
     * @return The space complexity
     */
    String getSpaceComplexity();
    
    /**
     * Gets the category of the algorithm (e.g., Sorting, Searching, etc.).
     * 
     * @return The algorithm category
     */
    AlgorithmCategory getCategory();
    
    /**
     * Enum representing different algorithm categories.
     */
    enum AlgorithmCategory {
        SORTING("Sorting"),
        SEARCHING("Searching"),
        GRAPH("Graph"),
        DYNAMIC_PROGRAMMING("Dynamic Programming");
        
        private final String displayName;
        
        AlgorithmCategory(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
}