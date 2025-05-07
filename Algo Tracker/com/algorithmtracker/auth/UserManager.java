package com.algorithmtracker.auth;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages user authentication, registration, and session management.
 * Supports both binary and text-based file formats for user data.
 */
public class UserManager {
    private Map<String, User> users;
    private User currentUser;
    private final String USER_DATA_FILE = "users.dat";
    private final String USER_TEXT_FILE = "users.txt";
    private boolean useTextFormat = true; // Set to true to use text format instead of binary
    
    /**
     * Constructs a new UserManager and loads existing user data.
     */
    public UserManager() {
        users = new HashMap<>();
        loadUsers();
    }
    
    /**
     * Constructs a new UserManager and loads existing user data.
     * 
     * @param useTextFormat if true, uses text format; if false, uses binary format
     */
    public UserManager(boolean useTextFormat) {
        users = new HashMap<>();
        this.useTextFormat = useTextFormat;
        loadUsers();
    }
    
    /**
     * Registers a new user.
     * 
     * @param username The username
     * @param password The password
     * @param email The email address
     * @param fullName The full name
     * @return true if registration is successful, false otherwise
     */
    public boolean registerUser(String username, String password, String email, String fullName) {
        // Check if username already exists
        if (users.containsKey(username)) {
            return false;
        }
        
        // Create new user
        User newUser = new User(username, password, email, fullName);
        users.put(username, newUser);
        
        // Save users to file
        saveUsers();
        
        return true;
    }
    
    /**
     * Authenticates a user.
     * 
     * @param username The username
     * @param password The password
     * @return true if authentication is successful, false otherwise
     */
    public boolean login(String username, String password) {
        User user = users.get(username);
        
        if (user != null && user.getPassword().equals(password)) {
            currentUser = user;
            return true;
        }
        
        return false;
    }
    
    /**
     * Logs out the current user.
     */
    public void logout() {
        currentUser = null;
    }
    
    /**
     * Gets the current logged-in user.
     * 
     * @return The current user, or null if no user is logged in
     */
    public User getCurrentUser() {
        return currentUser;
    }
    
    /**
     * Checks if a user is currently logged in.
     * 
     * @return true if a user is logged in, false otherwise
     */
    public boolean isLoggedIn() {
        return currentUser != null;
    }
    
    /**
     * Loads user data from file (either text or binary format).
     */
    @SuppressWarnings("unchecked")
    private void loadUsers() {
        if (useTextFormat) {
            loadUsersFromTextFile();
        } else {
            try {
                if (Files.exists(Paths.get(USER_DATA_FILE))) {
                    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USER_DATA_FILE))) {
                        users = (Map<String, User>) ois.readObject();
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error loading user data: " + e.getMessage());
                users = new HashMap<>();
            }
        }
    }
    
    /**
     * Saves user data to file (either text or binary format).
     */
    private void saveUsers() {
        if (useTextFormat) {
            saveUsersToTextFile();
        } else {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USER_DATA_FILE))) {
                oos.writeObject(users);
            } catch (IOException e) {
                System.err.println("Error saving user data: " + e.getMessage());
            }
        }
    }
    
    /**
     * Loads users from a text file.
     */
    private void loadUsersFromTextFile() {
        users.clear();
        try {
            if (Files.exists(Paths.get(USER_TEXT_FILE))) {
                BufferedReader reader = new BufferedReader(new FileReader(USER_TEXT_FILE));
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
            }
        } catch (IOException e) {
            System.err.println("Error loading user data from text file: " + e.getMessage());
            users = new HashMap<>();
        }
    }
    
    /**
     * Saves users to a text file.
     */
    private void saveUsersToTextFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(USER_TEXT_FILE))) {
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
     * Gets the number of registered users.
     * 
     * @return The number of users
     */
    public int getUserCount() {
        return users.size();
    }
    
    /**
     * Exports the current users to a text file, regardless of the current format setting.
     * 
     * @param filename The name of the file to export to
     * @return true if export was successful, false otherwise
     */
    public boolean exportToTextFile(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (User user : users.values()) {
                writer.println("USER: " + user.getUsername());
                writer.println("PASSWORD: " + user.getPassword());
                writer.println("EMAIL: " + user.getEmail());
                writer.println("FULLNAME: " + user.getFullName());
                writer.println(); // Empty line between users
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error exporting user data to text file: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Sets whether to use text format or binary format.
     * 
     * @param useTextFormat true to use text format, false to use binary format
     */
    public void setUseTextFormat(boolean useTextFormat) {
        this.useTextFormat = useTextFormat;
    }
    
    /**
     * Checks if the manager is using text format.
     * 
     * @return true if using text format, false if using binary format
     */
    public boolean isUsingTextFormat() {
        return useTextFormat;
    }
}