package com.algorithmtracker.ui;

import com.algorithmtracker.algorithm.*;
import com.algorithmtracker.algorithm.Algorithm.AlgorithmCategory;
import com.algorithmtracker.algorithm.GraphAlgorithms.Graph;
import com.algorithmtracker.auth.User;
import com.algorithmtracker.auth.UserManager;
import com.algorithmtracker.data.TestDataGenerator;
import com.algorithmtracker.performance.PerformanceTracker;
import com.algorithmtracker.performance.PerformanceTracker.PerformanceResult;
import com.algorithmtracker.result.ResultManager;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Handles user interaction and display formatting for the Algorithm Tracker.
 */
public class ConsoleUI {
    private Scanner scanner;
    private TestDataGenerator dataGenerator;
    private PerformanceTracker performanceTracker;
    private ResultManager resultManager;
    private UserManager userManager;
    
    // Algorithm instances
    private final SortingAlgorithms.BubbleSort bubbleSort = new SortingAlgorithms.BubbleSort();
    private final SortingAlgorithms.InsertionSort insertionSort = new SortingAlgorithms.InsertionSort();
    private final SortingAlgorithms.SelectionSort selectionSort = new SortingAlgorithms.SelectionSort();
    private final SortingAlgorithms.MergeSort mergeSort = new SortingAlgorithms.MergeSort();
    private final SortingAlgorithms.QuickSort quickSort = new SortingAlgorithms.QuickSort();
    
    private final SearchingAlgorithms.LinearSearch linearSearch = new SearchingAlgorithms.LinearSearch();
    private final SearchingAlgorithms.BinarySearch binarySearch = new SearchingAlgorithms.BinarySearch();
    
    private final GraphAlgorithms.DepthFirstSearch dfs = new GraphAlgorithms.DepthFirstSearch();
    private final GraphAlgorithms.BreadthFirstSearch bfs = new GraphAlgorithms.BreadthFirstSearch();
    private final GraphAlgorithms.DijkstraAlgorithm dijkstra = new GraphAlgorithms.DijkstraAlgorithm();
    
    /**
     * Constructs a ConsoleUI instance.
     */
    public ConsoleUI() {
        scanner = new Scanner(System.in);
        dataGenerator = new TestDataGenerator();
        performanceTracker = new PerformanceTracker();
        resultManager = new ResultManager();
        userManager = new UserManager();
    }
    
    /**
     * Displays a welcome message.
     */
    public void displayWelcomeMessage() {
        System.out.println("=======================================================");
        System.out.println("             ALGORITHM TRACKER");
        System.out.println("=======================================================");
        System.out.println("A tool for implementing, analyzing, and comparing");
        System.out.println("various algorithms.");
        System.out.println("=======================================================");
        System.out.println();
    }
    
    /**
     * Starts the authentication menu.
     */
    public void startAuthenticationMenu() {
        boolean exit = false;
        
        while (!exit) {
            System.out.println("\nAUTHENTICATION MENU");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("0. Exit");
            
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    registerUser();
                    break;
                case 2:
                    if (loginUser()) {
                        startMainMenu();
                        logoutUser();
                    }
                    break;
                case 0:
                    exit = true;
                    System.out.println("Thank you for using Algorithm Tracker. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    /**
     * Registers a new user.
     */
    private void registerUser() {
        System.out.println("\nUSER REGISTRATION");
        
        System.out.print("Enter username: ");
        String username = scanner.next();
        
        System.out.print("Enter password: ");
        String password = scanner.next();
        
        scanner.nextLine(); // Consume newline
        
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        
        System.out.print("Enter full name: ");
        String fullName = scanner.nextLine();
        
        boolean success = userManager.registerUser(username, password, email, fullName);
        
        if (success) {
            System.out.println("Registration successful! You can now login.");
        } else {
            System.out.println("Registration failed. Username may already exist.");
        }
    }
    
    /**
     * Logs in a user.
     * 
     * @return true if login is successful, false otherwise
     */
    private boolean loginUser() {
        System.out.println("\nUSER LOGIN");
        
        System.out.print("Enter username: ");
        String username = scanner.next();
        
        System.out.print("Enter password: ");
        String password = scanner.next();
        
        boolean success = userManager.login(username, password);
        
        if (success) {
            User currentUser = userManager.getCurrentUser();
            System.out.println("Login successful! Welcome, " + currentUser.getFullName() + "!");
            return true;
        } else {
            System.out.println("Login failed. Invalid username or password.");
            return false;
        }
    }
    
    /**
     * Logs out the current user.
     */
    private void logoutUser() {
        userManager.logout();
        System.out.println("\nYou have been logged out. Thank you for using Algorithm Tracker!");
    }
    
    /**
     * Starts the main menu system.
     */
    public void startMainMenu() {
        boolean exit = false;
        
        while (!exit) {
            displayMainMenu();
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    sortingMenu();
                    break;
                case 2:
                    searchingMenu();
                    break;
                case 3:
                    graphMenu();
                    break;
                case 5:
                    compareAlgorithmsMenu();
                    break;
                case 6:
                    viewResultsMenu();
                    break;
                case 7:
                    exportResultsMenu();
                    break;
                case 0:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    /**
     * Displays the main menu options.
     */
    private void displayMainMenu() {
        User currentUser = userManager.getCurrentUser();
        
        System.out.println("\nMAIN MENU - Logged in as: " + currentUser.getUsername());
        System.out.println("1. Sorting Algorithms");
        System.out.println("2. Searching Algorithms");
        System.out.println("3. Graph Algorithms");
        // Dynamic Programming option removed
        System.out.println("5. Compare Algorithms");
        System.out.println("6. View Results");
        System.out.println("7. Export Results");
        System.out.println("0. Logout");
    }
    
    /**
     * Displays the sorting algorithms menu.
     */
    private void sortingMenu() {
        boolean back = false;
        
        while (!back) {
            System.out.println("\nSORTING ALGORITHMS");
            System.out.println("1. Bubble Sort");
            System.out.println("2. Insertion Sort");
            System.out.println("3. Selection Sort");
            System.out.println("4. Merge Sort");
            System.out.println("5. Quick Sort");
            System.out.println("0. Back to Main Menu");
            
            int choice = getIntInput("Enter your choice: ");
            
            if (choice >= 1 && choice <= 5) {
                runSortingAlgorithm(choice);
            } else if (choice == 0) {
                back = true;
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    /**
     * Runs a sorting algorithm based on the user's choice.
     * 
     * @param choice The algorithm choice
     */
    private void runSortingAlgorithm(int choice) {
        System.out.println("\nSelect input type:");
        System.out.println("1. Random array");
        System.out.println("2. Nearly sorted array");
        System.out.println("3. Reversed array");
        System.out.println("4. Array with many duplicates");
        System.out.println("5. Custom input");
        
        int inputChoice = getIntInput("Enter your choice: ");
        int[] array;
        
        if (inputChoice >= 1 && inputChoice <= 4) {
            int size = getIntInput("Enter array size: ");
            
            switch (inputChoice) {
                case 1:
                    array = dataGenerator.generateRandomIntArray(size, 0, 1000);
                    break;
                case 2:
                    array = dataGenerator.generateNearlySortedIntArray(size, 0, 1000, 0.1);
                    break;
                case 3:
                    array = dataGenerator.generateReversedIntArray(size, 0, 1000);
                    break;
                case 4:
                    int uniqueValues = getIntInput("Enter number of unique values: ");
                    array = dataGenerator.generateArrayWithDuplicates(size, uniqueValues);
                    break;
                default:
                    array = new int[0];
            }
        } else if (inputChoice == 5) {
            array = getCustomIntArray();
        } else {
            System.out.println("Invalid choice. Using random array.");
            array = dataGenerator.generateRandomIntArray(10, 0, 100);
        }
        
        System.out.println("\nOriginal array: " + Arrays.toString(array));
        
        Algorithm algorithm;
        Function<int[], int[]> sortFunction;
        
        switch (choice) {
            case 1:
                algorithm = bubbleSort;
                sortFunction = bubbleSort::sort;
                break;
            case 2:
                algorithm = insertionSort;
                sortFunction = insertionSort::sort;
                break;
            case 3:
                algorithm = selectionSort;
                sortFunction = selectionSort::sort;
                break;
            case 4:
                algorithm = mergeSort;
                sortFunction = mergeSort::sort;
                break;
            case 5:
                algorithm = quickSort;
                sortFunction = quickSort::sort;
                break;
            default:
                System.out.println("Invalid choice. Using Bubble Sort.");
                algorithm = bubbleSort;
                sortFunction = bubbleSort::sort;
        }
        
        // Warm-up run to avoid JVM optimization effects
        sortFunction.apply(array);
        
        // Actual measurement
        long startTime = System.nanoTime();
        int[] sorted = sortFunction.apply(array);
        long endTime = System.nanoTime();
        long executionTime = (endTime - startTime) / 1_000_000; // Convert to milliseconds
        
        System.out.println("Sorted array: " + Arrays.toString(sorted));
        System.out.println("Execution time: " + executionTime + " ms");
        
        // Add result to performance tracker
        PerformanceResult result = new PerformanceResult(algorithm, executionTime, array.length);
        performanceTracker.addResult(result);
        resultManager.addResult(result);
        
        System.out.println("\nAlgorithm: " + algorithm.getName());
        System.out.println("Description: " + algorithm.getDescription());
        System.out.println("Time Complexity: " + algorithm.getTimeComplexity());
        System.out.println("Space Complexity: " + algorithm.getSpaceComplexity());
    }
    
    /**
     * Displays the searching algorithms menu.
     */
    private void searchingMenu() {
        boolean back = false;
        
        while (!back) {
            System.out.println("\nSEARCHING ALGORITHMS");
            System.out.println("1. Linear Search");
            System.out.println("2. Binary Search");
            System.out.println("0. Back to Main Menu");
            
            int choice = getIntInput("Enter your choice: ");
            
            if (choice >= 1 && choice <= 2) {
                runSearchingAlgorithm(choice);
            } else if (choice == 0) {
                back = true;
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    /**
     * Runs a searching algorithm based on the user's choice.
     * 
     * @param choice The algorithm choice
     */
    private void runSearchingAlgorithm(int choice) {
        System.out.println("\nSelect input type:");
        System.out.println("1. Random array");
        System.out.println("2. Custom input");
        
        int inputChoice = getIntInput("Enter your choice: ");
        int[] array;
        
        if (inputChoice == 1) {
            int size = getIntInput("Enter array size: ");
            array = dataGenerator.generateRandomIntArray(size, 0, 1000);
            
            // For binary search, the array needs to be sorted
            if (choice == 2) {
                Arrays.sort(array);
            }
        } else if (inputChoice == 2) {
            array = getCustomIntArray();
            
            // For binary search, the array needs to be sorted
            if (choice == 2) {
                Arrays.sort(array);
            }
        } else {
            System.out.println("Invalid choice. Using random array.");
            array = dataGenerator.generateRandomIntArray(10, 0, 100);
            
            // For binary search, the array needs to be sorted
            if (choice == 2) {
                Arrays.sort(array);
            }
        }
        
        System.out.println("\nArray: " + Arrays.toString(array));
        
        int target = getIntInput("Enter the value to search for: ");
        
        Algorithm algorithm;
        int result;
        long executionTime;
        
        if (choice == 1) {
            algorithm = linearSearch;
            
            // Warm-up run
            linearSearch.search(array, target);
            
            long startTime = System.nanoTime();
            result = linearSearch.search(array, target);
            long endTime = System.nanoTime();
            executionTime = (endTime - startTime) / 1_000_000; // Convert to milliseconds
        } else {
            algorithm = binarySearch;
            
            // Warm-up run
            binarySearch.search(array, target);
            
            long startTime = System.nanoTime();
            result = binarySearch.search(array, target);
            long endTime = System.nanoTime();
            executionTime = (endTime - startTime) / 1_000_000; // Convert to milliseconds
        }
        
        if (result != -1) {
            System.out.println("Element found at index: " + result);
        } else {
            System.out.println("Element not found in the array.");
        }
        
        System.out.println("Execution time: " + executionTime + " ms");
        
        // Add result to performance tracker
        PerformanceResult perfResult = new PerformanceResult(algorithm, executionTime, array.length);
        performanceTracker.addResult(perfResult);
        resultManager.addResult(perfResult);
        
        System.out.println("\nAlgorithm: " + algorithm.getName());
        System.out.println("Description: " + algorithm.getDescription());
        System.out.println("Time Complexity: " + algorithm.getTimeComplexity());
        System.out.println("Space Complexity: " + algorithm.getSpaceComplexity());
    }
    
    /**
     * Displays the graph algorithms menu.
     */
    private void graphMenu() {
        boolean back = false;
        
        while (!back) {
            System.out.println("\nGRAPH ALGORITHMS");
            System.out.println("1. Depth-First Search");
            System.out.println("2. Breadth-First Search");
            System.out.println("3. Dijkstra's Algorithm");
            System.out.println("0. Back to Main Menu");
            
            int choice = getIntInput("Enter your choice: ");
            
            if (choice >= 1 && choice <= 3) {
                runGraphAlgorithm(choice);
            } else if (choice == 0) {
                back = true;
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    /**
     * Runs a graph algorithm based on the user's choice.
     * 
     * @param choice The algorithm choice
     */
    private void runGraphAlgorithm(int choice) {
        System.out.println("\nSelect input type:");
        System.out.println("1. Random graph");
        System.out.println("2. Custom graph");
        
        int inputChoice = getIntInput("Enter your choice: ");
        Graph graph;
        
        if (inputChoice == 1) {
            int vertices = getIntInput("Enter number of vertices: ");
            int edges = getIntInput("Enter number of edges: ");
            int maxWeight = getIntInput("Enter maximum edge weight: ");
            
            graph = dataGenerator.generateRandomGraph(vertices, edges, maxWeight);
        } else if (inputChoice == 2) {
            graph = getCustomGraph();
        } else {
            System.out.println("Invalid choice. Using random graph.");
            graph = dataGenerator.generateRandomGraph(5, 8, 10);
        }
        
        printGraph(graph);
        
        Algorithm algorithm;
        long executionTime;
        
        int startVertex = getIntInput("Enter the starting vertex: ");
        
        if (startVertex < 0 || startVertex >= graph.getVertices()) {
            System.out.println("Invalid vertex. Using vertex 0.");
            startVertex = 0;
        }
        
        switch (choice) {
            case 1:
                algorithm = dfs;
                
                // Warm-up run
                dfs.traverse(graph, startVertex);
                
                long startTime1 = System.nanoTime();
                List<Integer> dfsResult = dfs.traverse(graph, startVertex);
                long endTime1 = System.nanoTime();
                executionTime = (endTime1 - startTime1) / 1_000_000; // Convert to milliseconds
                
                System.out.println("DFS traversal: " + dfsResult);
                break;
                
            case 2:
                algorithm = bfs;
                
                // Warm-up run
                bfs.traverse(graph, startVertex);
                
                long startTime2 = System.nanoTime();
                List<Integer> bfsResult = bfs.traverse(graph, startVertex);
                long endTime2 = System.nanoTime();
                executionTime = (endTime2 - startTime2) / 1_000_000; // Convert to milliseconds
                
                System.out.println("BFS traversal: " + bfsResult);
                break;
                
            case 3:
                algorithm = dijkstra;
                
                // Warm-up run
                dijkstra.findShortestPaths(graph, startVertex);
                
                long startTime3 = System.nanoTime();
                int[] distances = dijkstra.findShortestPaths(graph, startVertex);
                long endTime3 = System.nanoTime();
                executionTime = (endTime3 - startTime3) / 1_000_000; // Convert to milliseconds
                
                System.out.println("Shortest distances from vertex " + startVertex + ":");
                for (int i = 0; i < distances.length; i++) {
                    if (distances[i] == Integer.MAX_VALUE) {
                        System.out.println("Vertex " + i + ": Infinity (not reachable)");
                    } else {
                        System.out.println("Vertex " + i + ": " + distances[i]);
                    }
                }
                break;
                
            default:
                System.out.println("Invalid choice. Using DFS.");
                algorithm = dfs;
                
                // Warm-up run
                dfs.traverse(graph, startVertex);
                
                long startTime4 = System.nanoTime();
                List<Integer> defaultResult = dfs.traverse(graph, startVertex);
                long endTime4 = System.nanoTime();
                executionTime = (endTime4 - startTime4) / 1_000_000; // Convert to milliseconds
                
                System.out.println("DFS traversal: " + defaultResult);
        }
        
        System.out.println("Execution time: " + executionTime + " ms");
        
        // Add result to performance tracker
        PerformanceResult result = new PerformanceResult(algorithm, executionTime, graph.getVertices());
        performanceTracker.addResult(result);
        resultManager.addResult(result);
        
        System.out.println("\nAlgorithm: " + algorithm.getName());
        System.out.println("Description: " + algorithm.getDescription());
        System.out.println("Time Complexity: " + algorithm.getTimeComplexity());
        System.out.println("Space Complexity: " + algorithm.getSpaceComplexity());
    }
    
    /**
     * Displays the compare algorithms menu.
     */
    private void compareAlgorithmsMenu() {
        System.out.println("\nCOMPARE ALGORITHMS");
        System.out.println("Select algorithm category:");
        System.out.println("1. Sorting Algorithms");
        System.out.println("2. Searching Algorithms");
        System.out.println("3. Graph Algorithms");
        // Dynamic Programming option removed
        
        int categoryChoice = getIntInput("Enter your choice: ");
        
        switch (categoryChoice) {
            case 1:
                compareSortingAlgorithms();
                break;
            case 2:
                compareSearchingAlgorithms();
                break;
            case 3:
                compareGraphAlgorithms();
                break;
            default:
                System.out.println("Invalid choice. Returning to main menu.");
        }
    }
    
    /**
     * Compares sorting algorithms.
     */
    private void compareSortingAlgorithms() {
        System.out.println("\nComparing Sorting Algorithms");
        
        System.out.println("Select algorithms to compare (comma-separated, e.g., 1,3,5):");
        System.out.println("1. Bubble Sort");
        System.out.println("2. Insertion Sort");
        System.out.println("3. Selection Sort");
        System.out.println("4. Merge Sort");
        System.out.println("5. Quick Sort");
        
        String input = scanner.next();
        String[] choices = input.split(",");
        
        List<Algorithm> algorithms = new ArrayList<>();
        List<Function<int[], int[]>> functions = new ArrayList<>();
        
        for (String choice : choices) {
            try {
                int algorithmChoice = Integer.parseInt(choice.trim());
                
                switch (algorithmChoice) {
                    case 1:
                        algorithms.add(bubbleSort);
                        functions.add(bubbleSort::sort);
                        break;
                    case 2:
                        algorithms.add(insertionSort);
                        functions.add(insertionSort::sort);
                        break;
                    case 3:
                        algorithms.add(selectionSort);
                        functions.add(selectionSort::sort);
                        break;
                    case 4:
                        algorithms.add(mergeSort);
                        functions.add(mergeSort::sort);
                        break;
                    case 5:
                        algorithms.add(quickSort);
                        functions.add(quickSort::sort);
                        break;
                    default:
                        System.out.println("Invalid algorithm choice: " + algorithmChoice + ". Skipping.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input: " + choice + ". Skipping.");
            }
        }
        
        if (algorithms.isEmpty()) {
            System.out.println("No valid algorithms selected. Returning to main menu.");
            return;
        }
        
        System.out.println("\nSelect input type:");
        System.out.println("1. Random array");
        System.out.println("2. Nearly sorted array");
        System.out.println("3. Reversed array");
        System.out.println("4. Array with many duplicates");
        
        int inputChoice = getIntInput("Enter your choice: ");
        int size = getIntInput("Enter array size: ");
        
        int[] array;
        switch (inputChoice) {
            case 1:
                array = dataGenerator.generateRandomIntArray(size, 0, 1000);
                break;
            case 2:
                array = dataGenerator.generateNearlySortedIntArray(size, 0, 1000, 0.1);
                break;
            case 3:
                array = dataGenerator.generateReversedIntArray(size, 0, 1000);
                break;
            case 4:
                int uniqueValues = getIntInput("Enter number of unique values: ");
                array = dataGenerator.generateArrayWithDuplicates(size, uniqueValues);
                break;
            default:
                System.out.println("Invalid choice. Using random array.");
                array = dataGenerator.generateRandomIntArray(size, 0, 1000);
        }
        
        System.out.println("\nComparing " + algorithms.size() + " sorting algorithms on an array of size " + size);
        
        Map<String, Long> results = new HashMap<>();
        
        for (int i = 0; i < algorithms.size(); i++) {
            Algorithm algorithm = algorithms.get(i);
            Function<int[], int[]> function = functions.get(i);
            
            // Create a copy of the array for each algorithm
            int[] arrayCopy = Arrays.copyOf(array, array.length);
            
            // Warm-up run
            function.apply(arrayCopy);
            
            // Actual measurement
            long startTime = System.nanoTime();
            function.apply(arrayCopy);
            long endTime = System.nanoTime();
            long executionTime = (endTime - startTime) / 1_000_000; // Convert to milliseconds
            
            results.put(algorithm.getName(), executionTime);
            
            // Add result to performance tracker
            PerformanceResult result = new PerformanceResult(algorithm, executionTime, size);
            performanceTracker.addResult(result);
            resultManager.addResult(result);
        }
        
        displayComparisonResults(results);
    }
    
    /**
     * Compares searching algorithms.
     */
    private void compareSearchingAlgorithms() {
        System.out.println("\nComparing Searching Algorithms");
        
        List<Algorithm> algorithms = new ArrayList<>();
        algorithms.add(linearSearch);
        algorithms.add(binarySearch);
        
        System.out.println("\nSelect input type:");
        System.out.println("1. Random array");
        System.out.println("2. Sorted array");
        
        int inputChoice = getIntInput("Enter your choice: ");
        int size = getIntInput("Enter array size: ");
        
        int[] array;
        if (inputChoice == 1) {
            array = dataGenerator.generateRandomIntArray(size, 0, 1000);
        } else if (inputChoice == 2) {
            array = dataGenerator.generateSortedIntArray(size, 0, 1000);
        } else {
            System.out.println("Invalid choice. Using random array.");
            array = dataGenerator.generateRandomIntArray(size, 0, 1000);
        }
        
        // For binary search, we need a sorted array
        int[] sortedArray = Arrays.copyOf(array, array.length);
        Arrays.sort(sortedArray);
        
        int target = getIntInput("Enter the value to search for: ");
        
        System.out.println("\nComparing searching algorithms on an array of size " + size);
        
        Map<String, Long> results = new HashMap<>();
        
        // Linear Search
        // Warm-up run
        linearSearch.search(array, target);
        
        long startTime1 = System.nanoTime();
        linearSearch.search(array, target);
        long endTime1 = System.nanoTime();
        long linearTime = (endTime1 - startTime1) / 1_000_000; // Convert to milliseconds
        
        results.put(linearSearch.getName(), linearTime);
        
        // Add result to performance tracker
        PerformanceResult linearResult = new PerformanceResult(linearSearch, linearTime, size);
        performanceTracker.addResult(linearResult);
        resultManager.addResult(linearResult);
        
        // Binary Search (on sorted array)
        // Warm-up run
        binarySearch.search(sortedArray, target);
        
        long startTime2 = System.nanoTime();
        binarySearch.search(sortedArray, target);
        long endTime2 = System.nanoTime();
        long binaryTime = (endTime2 - startTime2) / 1_000_000; // Convert to milliseconds
        
        results.put(binarySearch.getName(), binaryTime);
        
        // Add result to performance tracker
        PerformanceResult binaryResult = new PerformanceResult(binarySearch, binaryTime, size);
        performanceTracker.addResult(binaryResult);
        resultManager.addResult(binaryResult);
        
        displayComparisonResults(results);
    }
    
    /**
     * Compares graph algorithms.
     */
    private void compareGraphAlgorithms() {
        System.out.println("\nComparing Graph Algorithms");
        
        System.out.println("Select algorithms to compare (comma-separated, e.g., 1,2):");
        System.out.println("1. Depth-First Search");
        System.out.println("2. Breadth-First Search");
        System.out.println("3. Dijkstra's Algorithm");
        
        String input = scanner.next();
        String[] choices = input.split(",");
        
        List<Algorithm> algorithms = new ArrayList<>();
        
        for (String choice : choices) {
            try {
                int algorithmChoice = Integer.parseInt(choice.trim());
                
                switch (algorithmChoice) {
                    case 1:
                        algorithms.add(dfs);
                        break;
                    case 2:
                        algorithms.add(bfs);
                        break;
                    case 3:
                        algorithms.add(dijkstra);
                        break;
                    default:
                        System.out.println("Invalid algorithm choice: " + algorithmChoice + ". Skipping.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input: " + choice + ". Skipping.");
            }
        }
        
        if (algorithms.isEmpty()) {
            System.out.println("No valid algorithms selected. Returning to main menu.");
            return;
        }
        
        int vertices = getIntInput("Enter number of vertices: ");
        int edges = getIntInput("Enter number of edges: ");
        int maxWeight = getIntInput("Enter maximum edge weight: ");
        
        Graph graph = dataGenerator.generateRandomGraph(vertices, edges, maxWeight);
        
        printGraph(graph);
        
        int startVertex = getIntInput("Enter the starting vertex: ");
        
        if (startVertex < 0 || startVertex >= graph.getVertices()) {
            System.out.println("Invalid vertex. Using vertex 0.");
            startVertex = 0;
        }
        
        System.out.println("\nComparing graph algorithms on a graph with " + vertices + " vertices and " + edges + " edges");
        
        Map<String, Long> results = new HashMap<>();
        
        for (Algorithm algorithm : algorithms) {
            long executionTime;
            
            if (algorithm == dfs) {
                // Warm-up run
                dfs.traverse(graph, startVertex);
                
                long startTime = System.nanoTime();
                dfs.traverse(graph, startVertex);
                long endTime = System.nanoTime();
                executionTime = (endTime - startTime) / 1_000_000; // Convert to milliseconds
            } else if (algorithm == bfs) {
                // Warm-up run
                bfs.traverse(graph, startVertex);
                
                long startTime = System.nanoTime();
                bfs.traverse(graph, startVertex);
                long endTime = System.nanoTime();
                executionTime = (endTime - startTime) / 1_000_000; // Convert to milliseconds
            } else { // Dijkstra
                // Warm-up run
                dijkstra.findShortestPaths(graph, startVertex);
                
                long startTime = System.nanoTime();
                dijkstra.findShortestPaths(graph, startVertex);
                long endTime = System.nanoTime();
                executionTime = (endTime - startTime) / 1_000_000; // Convert to milliseconds
            }
            
            results.put(algorithm.getName(), executionTime);
            
            // Add result to performance tracker
            PerformanceResult result = new PerformanceResult(algorithm, executionTime, vertices);
            performanceTracker.addResult(result);
            resultManager.addResult(result);
        }
        
        displayComparisonResults(results);
    }
    
    /**
     * Displays the results of algorithm comparisons.
     * 
     * @param results The map of algorithm names to execution times
     */
    private void displayComparisonResults(Map<String, Long> results) {
        System.out.println("\nCOMPARISON RESULTS");
        System.out.println("--------------------------------------------------");
        System.out.printf("%-30s %-15s\n", "Algorithm", "Execution Time (ms)");
        System.out.println("--------------------------------------------------");
        
        // Sort results by execution time
        List<Map.Entry<String, Long>> sortedResults = results.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toList());
        
        for (Map.Entry<String, Long> entry : sortedResults) {
            System.out.printf("%-30s %-15d\n", entry.getKey(), entry.getValue());
        }
        
        System.out.println("--------------------------------------------------");
        
        // Find the fastest algorithm
        Map.Entry<String, Long> fastest = sortedResults.get(0);
        System.out.println("Fastest algorithm: " + fastest.getKey() + " (" + fastest.getValue() + " ms)");
        
        // Calculate speedup compared to the slowest
        Map.Entry<String, Long> slowest = sortedResults.get(sortedResults.size() - 1);
        double speedup = (double) slowest.getValue() / fastest.getValue();
        System.out.printf("Speedup compared to slowest: %.2fx\n", speedup);
    }
    
    /**
     * Displays the view results menu.
     */
    private void viewResultsMenu() {
        boolean back = false;
        
        while (!back) {
            System.out.println("\nVIEW RESULTS");
            System.out.println("1. View Current Session Results");
            System.out.println("2. View All Results");
            System.out.println("3. View Results by Algorithm Category");
            System.out.println("0. Back to Main Menu");
            
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    System.out.println("\n" + resultManager.generateCurrentSessionSummary());
                    break;
                case 2:
                    List<String> sessions = resultManager.getSessionNames();
                    
                    if (sessions.isEmpty()) {
                        System.out.println("No sessions available.");
                    } else {
                        System.out.println("\nAll Sessions:");
                        for (int i = 0; i < sessions.size(); i++) {
                            System.out.println((i + 1) + ". " + sessions.get(i));
                        }
                        
                        int sessionChoice = getIntInput("Enter session number to view (0 to view all): ");
                        
                        if (sessionChoice == 0) {
                            for (String session : sessions) {
                                System.out.println("\n" + resultManager.generateSessionSummary(session));
                            }
                        } else if (sessionChoice > 0 && sessionChoice <= sessions.size()) {
                            String session = sessions.get(sessionChoice - 1);
                            System.out.println("\n" + resultManager.generateSessionSummary(session));
                        } else {
                            System.out.println("Invalid session number.");
                        }
                    }
                    break;
                case 3:
                    System.out.println("\nSelect algorithm category:");
                    System.out.println("1. Sorting");
                    System.out.println("2. Searching");
                    System.out.println("3. Graph");
                    // Dynamic Programming option removed
                    
                    int categoryChoice = getIntInput("Enter your choice: ");
                    AlgorithmCategory category;
                    
                    switch (categoryChoice) {
                        case 1:
                            category = AlgorithmCategory.SORTING;
                            break;
                        case 2:
                            category = AlgorithmCategory.SEARCHING;
                            break;
                        case 3:
                            category = AlgorithmCategory.GRAPH;
                            break;
                        default:
                            System.out.println("Invalid category. Using Sorting.");
                            category = AlgorithmCategory.SORTING;
                    }
                    
                    List<PerformanceResult> results = performanceTracker.getResults();
                    List<PerformanceResult> categoryResults = results.stream()
                            .filter(r -> r.getAlgorithm().getCategory() == category)
                            .collect(Collectors.toList());
                    
                    if (categoryResults.isEmpty()) {
                        System.out.println("No results available for this category.");
                    } else {
                        System.out.println("\nResults for " + category.getDisplayName() + " Algorithms:");
                        System.out.println("--------------------------------------------------");
                        System.out.printf("%-25s %-15s %-15s\n", "Algorithm", "Input Size", "Execution Time (ms)");
                        System.out.println("--------------------------------------------------");
                        
                        for (PerformanceResult result : categoryResults) {
                            System.out.printf("%-25s %-15d %-15d\n",
                                    result.getAlgorithm().getName(),
                                    result.getInputSize(),
                                    result.getExecutionTime());
                        }
                        
                        System.out.println("--------------------------------------------------");
                    }
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    /**
     * Displays the export results menu.
     */
    private void exportResultsMenu() {
        boolean back = false;
        
        while (!back) {
            System.out.println("\nEXPORT RESULTS");
            System.out.println("1. Export Current Session Results");
            System.out.println("2. Export All Results");
            System.out.println("0. Back to Main Menu");
            
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    System.out.println("Enter file path for export (e.g., results/current_session.csv):");
                    scanner.nextLine(); // Consume newline
                    String currentPath = scanner.nextLine();
                    
                    try {
                        resultManager.exportCurrentSessionToCSV(currentPath);
                        System.out.println("Results exported successfully to " + currentPath);
                    } catch (IOException e) {
                        System.out.println("Error exporting results: " + e.getMessage());
                    }
                    break;
                case 2:
                    System.out.println("Enter file path for export (e.g., results/all_results.csv):");
                    scanner.nextLine(); // Consume newline
                    String allPath = scanner.nextLine();
                    
                    try {
                        resultManager.exportAllResultsToCSV(allPath);
                        System.out.println("Results exported successfully to " + allPath);
                    } catch (IOException e) {
                        System.out.println("Error exporting results: " + e.getMessage());
                    }
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    /**
     * Gets an integer input from the user.
     * 
     * @param prompt The prompt to display
     * @return The integer input
     */
    private int getIntInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter an integer.");
            scanner.next(); // Consume invalid input
            System.out.print(prompt);
        }
        return scanner.nextInt();
    }
    
    /**
     * Gets a custom integer array from the user.
     * 
     * @return The custom array
     */
    private int[] getCustomIntArray() {
        System.out.println("Enter the size of the array:");
        int size = scanner.nextInt();
        
        int[] array = new int[size];
        
        System.out.println("Enter " + size + " integers (space-separated):");
        for (int i = 0; i < size; i++) {
            array[i] = scanner.nextInt();
        }
        
        return array;
    }
    
    /**
     * Gets a custom graph from the user.
     * 
     * @return The custom graph
     */
    private Graph getCustomGraph() {
        System.out.println("Enter the number of vertices:");
        int vertices = scanner.nextInt();
        
        Graph graph = new Graph(vertices);
        
        System.out.println("Enter the number of edges:");
        int edges = scanner.nextInt();
        
        System.out.println("Enter " + edges + " edges in the format 'source destination weight':");
        for (int i = 0; i < edges; i++) {
            int source = scanner.nextInt();
            int destination = scanner.nextInt();
            int weight = scanner.nextInt();
            
            if (source >= 0 && source < vertices && destination >= 0 && destination < vertices) {
                graph.addEdge(source, destination, weight);
            } else {
                System.out.println("Invalid edge. Vertices must be between 0 and " + (vertices - 1) + ".");
                i--; // Retry this edge
            }
        }
        
        return graph;
    }
    
    /**
     * Prints a graph.
     * 
     * @param graph The graph to print
     */
    private void printGraph(Graph graph) {
        System.out.println("\nGraph Representation (Adjacency List):");
        
        for (int i = 0; i < graph.getVertices(); i++) {
            System.out.print("Vertex " + i + " -> ");
            
            List<Graph.Edge> edges = graph.getAdjacencyList().get(i);
            
            if (edges.isEmpty()) {
                System.out.println("No outgoing edges");
            } else {
                for (int j = 0; j < edges.size(); j++) {
                    Graph.Edge edge = edges.get(j);
                    System.out.print(edge.getDestination() + " (weight: " + edge.getWeight() + ")");
                    
                    if (j < edges.size() - 1) {
                        System.out.print(", ");
                    }
                }
                System.out.println();
            }
        }
    }
}
