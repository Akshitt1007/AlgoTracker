package com.algorithmtracker.auth;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for exporting users from a binary format to a text format.
 */
public class UserExporter {
    
    /**
     * Main method to demonstrate exporting users.
     * 
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        // Create some sample users
        createSampleUsers();
        
        // Export existing users.dat to users.txt
        exportBinaryToText("users.dat", "users.txt");
        
        System.out.println("User data has been exported to users.txt");
    }
    
    /**
     * Create sample users and save to binary format.
     */
    private static void createSampleUsers() {
        Map<String, User> users = new HashMap<>();
        
        // Create some sample users
        users.put("alice", new User("alice", "password123", "alice@example.com", "Alice Johnson"));
        users.put("bob", new User("bob", "securepass", "bob@example.com", "Bob Smith"));
        users.put("charlie", new User("charlie", "pass1234", "charlie@example.com", "Charlie Brown"));
        
        // Save to binary file
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("users.dat"))) {
            oos.writeObject(users);
            System.out.println("Sample users created and saved to users.dat");
        } catch (IOException e) {
            System.err.println("Error creating sample user data: " + e.getMessage());
        }
    }
    
    /**
     * Export users from binary format to text format.
     * 
     * @param binaryFile The binary file to read from
     * @param textFile The text file to write to
     */
    @SuppressWarnings("unchecked")
    public static void exportBinaryToText(String binaryFile, String textFile) {
        Map<String, User> users = new HashMap<>();
        
        // Load users from binary file
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(binaryFile))) {
            users = (Map<String, User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading user data: " + e.getMessage());
            return;
        }
        
        // Save users to text file
        try (PrintWriter writer = new PrintWriter(new FileWriter(textFile))) {
            for (User user : users.values()) {
                writer.println("USER: " + user.getUsername());
                writer.println("PASSWORD: " + user.getPassword());
                writer.println("EMAIL: " + user.getEmail());
                writer.println("FULLNAME: " + user.getFullName());
                writer.println(); // Empty line between users
            }
        } catch (IOException e) {
            System.err.println("Error saving user data to text file: " + e.getMessage());
        }
    }
    
    /**
     * Import users from text format to binary format.
     * 
     * @param textFile The text file to read from
     * @param binaryFile The binary file to write to
     */
    public static void importTextToBinary(String textFile, String binaryFile) {
        Map<String, User> users = new HashMap<>();
        
        try {
            BufferedReader reader = new BufferedReader(new FileReader(textFile));
            String line;
            User currentUser = null;
            
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("USER:")) {
                    // If we were processing a user, add them to the map
                    if (currentUser != null) {
                        users.put(currentUser.getUsername(), currentUser);
                    }
                    
                    // Start a new user
                    String username = line.substring(5).trim();
                    currentUser = new User();
                    currentUser.setUsername(username);
                } else if (currentUser != null) {
                    if (line.startsWith("PASSWORD:")) {
                        currentUser.setPassword(line.substring(9).trim());
                    } else if (line.startsWith("EMAIL:")) {
                        currentUser.setEmail(line.substring(6).trim());
                    } else if (line.startsWith("FULLNAME:")) {
                        currentUser.setFullName(line.substring(9).trim());
                    }
                }
            }
            
            // Add the last user if there is one
            if (currentUser != null) {
                users.put(currentUser.getUsername(), currentUser);
            }
            
            reader.close();
            
            // Save to binary file
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(binaryFile))) {
                oos.writeObject(users);
                System.out.println("Users imported from text file and saved to binary file");
            } catch (IOException e) {
                System.err.println("Error saving user data to binary file: " + e.getMessage());
            }
            
        } catch (IOException e) {
            System.err.println("Error loading user data from text file: " + e.getMessage());
        }
    }
}