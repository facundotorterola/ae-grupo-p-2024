package Optimimzacion.problem;

import Optimimzacion.modelo.Graph;
import org.uma.jmetal.problem.impl.AbstractIntegerPermutationProblem;
import org.uma.jmetal.solution.PermutationSolution;

public class ShortestPathProblem extends AbstractIntegerPermutationProblem {
    private final Graph graph;

    public ShortestPathProblem(Graph graph) {
        this.graph = graph;
        setNumberOfVariables(graph.getNumberOfNodes()); // Número de nodos
        setNumberOfObjectives(1);                      // Minimizar una sola métrica
        setName("ShortestPath");
    }

    @Override
    public void evaluate(PermutationSolution<Integer> solution) {
        int totalDistance = 0;

        for (int i = 0; i < solution.getNumberOfVariables() - 1; i++) {
            int from = solution.getVariableValue(i);
            int to = solution.getVariableValue(i+1);
            totalDistance += graph.getDistance(from, to);
        }

        // Regresar al nodo inicial para formar un ciclo
        int last = solution.getVariableValue(solution.getNumberOfVariables() - 1);
        int first = solution.getVariableValue(0);
        totalDistance += graph.getDistance(last, first);

        solution.setObjective(0, totalDistance);
    }

    @Override
    public int getPermutationLength() {
        return graph.getNumberOfNodes();
    }
}