package Optimimzacion.problem;

import Optimimzacion.modelo.GrafoContenedores;
import org.uma.jmetal.problem.impl.AbstractIntegerPermutationProblem;
import org.uma.jmetal.solution.PermutationSolution;

import java.util.Map;

public class ShortestPathProblemContenedores extends AbstractIntegerPermutationProblem {
    private final GrafoContenedores graph;

    public ShortestPathProblemContenedores(GrafoContenedores graph) {
        this.graph = graph;
        setNumberOfVariables(graph.getCantidadNodos()); // Número de nodos
        setNumberOfObjectives(1);                      // Minimizar una sola métrica
        setName("ShortestPathContenedores");
    }

    @Override
    public void evaluate(PermutationSolution<Integer> solution) {
        double totalDistance = 0;

        for (int i = 0; i < solution.getNumberOfVariables() - 1; i++) {
            int from = solution.getVariableValue(i);
            int to = solution.getVariableValue(i+1);
            totalDistance += this.graph.calcularDistanciasNodo(from, to);
            System.out.println("Distancia de " + from + " a " + to + " es " + this.graph.calcularDistanciasNodo(from, to));
            System.out.println("Distancia total: " + totalDistance);
        }

        // Regresar al nodo inicial para formar un ciclo
        int last = solution.getVariableValue(solution.getNumberOfVariables() - 1);
        int first = solution.getVariableValue(0);
        totalDistance += this.graph.calcularDistanciasNodo(last, first);

        solution.setObjective(0, totalDistance);
    }

    @Override
    public int getPermutationLength() {
        return graph.getCantidadNodos();
    }
}