package Optimimzacion.algorithm;

import Optimimzacion.modelo.Graph;
import Optimimzacion.problem.ShortestPathProblem;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.crossover.PMXCrossover;
import org.uma.jmetal.operator.impl.mutation.PermutationSwapMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.PermutationSolution;
import org.uma.jmetal.util.AlgorithmRunner;

import java.util.List;

public class RunShortestPathProblem {
    public static void main(String[] args) {
        // Definir el grafo
        int[][] adjacencyMatrix = {
                {0, 100, 15, 20},
                {100, 0, 35, 25},
                {15, 35, 0, 30},
                {20, 25, 30, 0}
        };
        Graph graph = new Graph(adjacencyMatrix);

        // Crear el problema
        ShortestPathProblem problem = new ShortestPathProblem(graph);

        // Operadores genéticos
        var crossover = new PMXCrossover(0.9);
        PermutationSwapMutation mutation = new PermutationSwapMutation<>(1.0 / problem.getNumberOfVariables());
//        var selection = new BinaryTournamentSelection<>();

        // Configurar el algoritmo
        var algorithm = new NSGAIIBuilder<>(problem, crossover, mutation)
                .setMaxIterations(10000)
                .setPopulationSize(100)
                .build();

        // Ejecutar el algoritmo
        new AlgorithmRunner.Executor(algorithm).execute();

        // Mostrar resultados
        List<PermutationSolution<Integer>> population = (List<PermutationSolution<Integer>>) algorithm.getResult();
        PermutationSolution<Integer> bestSolution = population.get(0);
        for (PermutationSolution<Integer> solution : population) {
            if (solution.getObjective(0) < bestSolution.getObjective(0)) {
                bestSolution = solution;
            }

        }
            System.out.println("====================================");
            System.out.print("Solution: [ ");
            for (int i = 0; i < bestSolution.getNumberOfVariables(); i++) {
                System.out.print(bestSolution.getVariableValue(i) + " ");
            }
            System.out.println("]");
            System.out.println("Total Distance: " + bestSolution.getObjective(0));
            System.out.println("====================================");
    }
}