package com.algorithmtracker.algorithm;

import java.util.*;

/**
 * Implementation of various graph algorithms.
 * Includes Depth-First Search, Breadth-First Search, and Dijkstra's Algorithm.
 */
public class GraphAlgorithms {
    
    /**
     * Represents a graph using an adjacency list.
     */
    public static class Graph {
        private int vertices;
        private List<List<Edge>> adjacencyList;
        
        /**
         * Constructs a graph with the specified number of vertices.
         * 
         * @param vertices The number of vertices in the graph
         */
        public Graph(int vertices) {
            this.vertices = vertices;
            adjacencyList = new ArrayList<>(vertices);
            
            for (int i = 0; i < vertices; i++) {
                adjacencyList.add(new ArrayList<>());
            }
        }
        
        /**
         * Adds an edge to the graph.
         * 
         * @param source The source vertex
         * @param destination The destination vertex
         * @param weight The weight of the edge
         */
        public void addEdge(int source, int destination, int weight) {
            Edge edge = new Edge(source, destination, weight);
            adjacencyList.get(source).add(edge);
        }
        
        /**
         * Gets the adjacency list of the graph.
         * 
         * @return The adjacency list
         */
        public List<List<Edge>> getAdjacencyList() {
            return adjacencyList;
        }
        
        /**
         * Gets the number of vertices in the graph.
         * 
         * @return The number of vertices
         */
        public int getVertices() {
            return vertices;
        }
        
        /**
         * Represents an edge in the graph.
         */
        public static class Edge {
            private int source;
            private int destination;
            private int weight;
            
            /**
             * Constructs an edge with the specified source, destination, and weight.
             * 
             * @param source The source vertex
             * @param destination The destination vertex
             * @param weight The weight of the edge
             */
            public Edge(int source, int destination, int weight) {
                this.source = source;
                this.destination = destination;
                this.weight = weight;
            }
            
            /**
             * Gets the source vertex of the edge.
             * 
             * @return The source vertex
             */
            public int getSource() {
                return source;
            }
            
            /**
             * Gets the destination vertex of the edge.
             * 
             * @return The destination vertex
             */
            public int getDestination() {
                return destination;
            }
            
            /**
             * Gets the weight of the edge.
             * 
             * @return The weight
             */
            public int getWeight() {
                return weight;
            }
        }
    }
    
    /**
     * Depth-First Search implementation.
     */
    public static class DepthFirstSearch implements Algorithm {
        
        /**
         * Performs a Depth-First Search traversal of the graph starting from the specified vertex.
         * 
         * @param graph The graph to traverse
         * @param startVertex The starting vertex
         * @return A list of vertices in the order they were visited
         */
        public List<Integer> traverse(Graph graph, int startVertex) {
            boolean[] visited = new boolean[graph.getVertices()];
            List<Integer> result = new ArrayList<>();
            
            // Call the recursive helper function
            dfsUtil(graph, startVertex, visited, result);
            
            return result;
        }
        
        /**
         * Recursive utility function for DFS.
         * 
         * @param graph The graph to traverse
         * @param vertex The current vertex
         * @param visited Array to track visited vertices
         * @param result List to store the traversal result
         */
        private void dfsUtil(Graph graph, int vertex, boolean[] visited, List<Integer> result) {
            // Mark the current node as visited and add to result
            visited[vertex] = true;
            result.add(vertex);
            
            // Recur for all the vertices adjacent to this vertex
            for (Graph.Edge edge : graph.getAdjacencyList().get(vertex)) {
                int neighbor = edge.getDestination();
                if (!visited[neighbor]) {
                    dfsUtil(graph, neighbor, visited, result);
                }
            }
        }
        
        @Override
        public String getName() {
            return "Depth-First Search";
        }
        
        @Override
        public String getDescription() {
            return "A graph traversal algorithm that explores as far as possible along each branch before backtracking.";
        }
        
        @Override
        public String getTimeComplexity() {
            return "O(V + E) where V is the number of vertices and E is the number of edges";
        }
        
        @Override
        public String getSpaceComplexity() {
            return "O(V)";
        }
        
        @Override
        public AlgorithmCategory getCategory() {
            return AlgorithmCategory.GRAPH;
        }
    }
    
    /**
     * Breadth-First Search implementation.
     */
    public static class BreadthFirstSearch implements Algorithm {
        
        /**
         * Performs a Breadth-First Search traversal of the graph starting from the specified vertex.
         * 
         * @param graph The graph to traverse
         * @param startVertex The starting vertex
         * @return A list of vertices in the order they were visited
         */
        public List<Integer> traverse(Graph graph, int startVertex) {
            boolean[] visited = new boolean[graph.getVertices()];
            List<Integer> result = new ArrayList<>();
            Queue<Integer> queue = new LinkedList<>();
            
            // Mark the current node as visited and enqueue it
            visited[startVertex] = true;
            queue.add(startVertex);
            
            while (!queue.isEmpty()) {
                // Dequeue a vertex from queue and add to result
                int vertex = queue.poll();
                result.add(vertex);
                
                // Get all adjacent vertices of the dequeued vertex
                // If an adjacent vertex has not been visited, mark it as visited and enqueue it
                for (Graph.Edge edge : graph.getAdjacencyList().get(vertex)) {
                    int neighbor = edge.getDestination();
                    if (!visited[neighbor]) {
                        visited[neighbor] = true;
                        queue.add(neighbor);
                    }
                }
            }
            
            return result;
        }
        
        @Override
        public String getName() {
            return "Breadth-First Search";
        }
        
        @Override
        public String getDescription() {
            return "A graph traversal algorithm that explores all the vertices of a graph at the present depth prior to moving on to vertices at the next depth level.";
        }
        
        @Override
        public String getTimeComplexity() {
            return "O(V + E) where V is the number of vertices and E is the number of edges";
        }
        
        @Override
        public String getSpaceComplexity() {
            return "O(V)";
        }
        
        @Override
        public AlgorithmCategory getCategory() {
            return AlgorithmCategory.GRAPH;
        }
    }
    
    /**
     * Dijkstra's Algorithm implementation.
     */
    public static class DijkstraAlgorithm implements Algorithm {
        
        /**
         * Finds the shortest paths from the source vertex to all other vertices using Dijkstra's algorithm.
         * 
         * @param graph The graph
         * @param sourceVertex The source vertex
         * @return An array of shortest distances from the source vertex to all other vertices
         */
        public int[] findShortestPaths(Graph graph, int sourceVertex) {
            int vertices = graph.getVertices();
            int[] distances = new int[vertices];
            boolean[] visited = new boolean[vertices];
            
            // Initialize all distances as INFINITE and visited[] as false
            Arrays.fill(distances, Integer.MAX_VALUE);
            distances[sourceVertex] = 0;
            
            // Find shortest path for all vertices
            for (int count = 0; count < vertices - 1; count++) {
                // Pick the minimum distance vertex from the set of vertices not yet processed
                int u = minDistance(distances, visited);
                
                // Mark the picked vertex as processed
                visited[u] = true;
                
                // Update distance value of the adjacent vertices of the picked vertex
                for (Graph.Edge edge : graph.getAdjacencyList().get(u)) {
                    int v = edge.getDestination();
                    
                    // Update distance[v] only if it's not in visited, there is an edge from u to v,
                    // and total weight of path from source to v through u is smaller than current value of distance[v]
                    if (!visited[v] && distances[u] != Integer.MAX_VALUE && 
                            distances[u] + edge.getWeight() < distances[v]) {
                        distances[v] = distances[u] + edge.getWeight();
                    }
                }
            }
            
            return distances;
        }
        
        /**
         * Finds the vertex with the minimum distance value from the set of vertices not yet included in the shortest path tree.
         * 
         * @param distances Array of distances
         * @param visited Array of visited vertices
         * @return The vertex with the minimum distance
         */
        private int minDistance(int[] distances, boolean[] visited) {
            int min = Integer.MAX_VALUE;
            int minIndex = -1;
            
            for (int v = 0; v < distances.length; v++) {
                if (!visited[v] && distances[v] <= min) {
                    min = distances[v];
                    minIndex = v;
                }
            }
            
            return minIndex;
        }
        
        @Override
        public String getName() {
            return "Dijkstra's Algorithm";
        }
        
        @Override
        public String getDescription() {
            return "An algorithm for finding the shortest paths between nodes in a graph, which may represent, for example, road networks.";
        }
        
        @Override
        public String getTimeComplexity() {
            return "O(V²) with adjacency matrix, O(E + V log V) with min-priority queue";
        }
        
        @Override
        public String getSpaceComplexity() {
            return "O(V)";
        }
        
        @Override
        public AlgorithmCategory getCategory() {
            return AlgorithmCategory.GRAPH;
        }
    }
}