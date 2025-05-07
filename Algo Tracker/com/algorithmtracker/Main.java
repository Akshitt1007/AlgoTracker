package com.algorithmtracker;

import com.algorithmtracker.ui.ConsoleUI;

/**
 * Main entry point for the Algorithm Tracker application.
 * This class initializes the application and starts the main menu system.
 * 
 * @author Algorithm Tracker
 * @version 2.0
 */
public class Main {
    
    /**
     * The main method that serves as the entry point for the application.
     * 
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        ConsoleUI ui = new ConsoleUI();
        ui.displayWelcomeMessage();
        ui.startAuthenticationMenu();
    }
}