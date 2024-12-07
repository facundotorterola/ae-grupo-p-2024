package Optimimzacion.modelo;

public class Graph {
    private final int[][] adjacencyMatrix;

    public Graph(int[][] adjacencyMatrix) {
        this.adjacencyMatrix = adjacencyMatrix;
    }

    public int getDistance(int from, int to) {
        return adjacencyMatrix[from][to];
    }

    public int getNumberOfNodes() {
        return adjacencyMatrix.length;
    }
}