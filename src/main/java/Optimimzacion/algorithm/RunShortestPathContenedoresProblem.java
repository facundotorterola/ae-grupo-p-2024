package Optimimzacion.algorithm;

import Optimimzacion.modelo.Contenedor;
import Optimimzacion.modelo.GrafoContenedores;
import Optimimzacion.modelo.Graph;
import Optimimzacion.problem.ShortestPathProblemContenedores;
import Optimimzacion.utils.LeerCSV;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.crossover.PMXCrossover;
import org.uma.jmetal.operator.impl.mutation.PermutationSwapMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.PermutationSolution;
import org.uma.jmetal.util.AlgorithmRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RunShortestPathContenedoresProblem {
    public static void main(String[] args) {


        Map<Integer, Contenedor> contenedores = LeerCSV.getContenedores();
//        Map<Integer, Contenedor> contenedores = new HashMap<>();
//
//        contenedores.put(1, new Contenedor(0, -34.87333, -56.24022));
//        contenedores.put(2, new Contenedor(1, -34.87412, -56.23973));
//        contenedores.put(3, new Contenedor(2, -34.875416, -56.239506));
//        contenedores.put(4, new Contenedor(3, -34.8755, -56.23801));
//        contenedores.put(5, new Contenedor(4, -34.874973, -56.237198));
//        contenedores.put(6, new Contenedor(5, -34.874317, -56.236416));
//        contenedores.put(7, new Contenedor(6, -34.87323, -56.23591));
//        contenedores.put(8, new Contenedor(7, -34.87362, -56.237));
//        contenedores.put(9, new Contenedor(8, -34.873592, -56.237022));
//        contenedores.put(10, new Contenedor(9, -34.874096, -56.237915));
//        contenedores.put(11, new Contenedor(10, -34.873432, -56.238754));
//        contenedores.put(12, new Contenedor(11, -34.86912, -56.23935));
//        contenedores.put(13, new Contenedor(12, -34.86977, -56.238598));
//        contenedores.put(14, new Contenedor(13, -34.87083, -56.23736));
//        contenedores.put(15, new Contenedor(14, -34.871437, -56.236637));
        GrafoContenedores grafoContenedores = new GrafoContenedores(contenedores);
        // Crear el problema
        ShortestPathProblemContenedores problem = new ShortestPathProblemContenedores(grafoContenedores);

        // Operadores genéticos
        var crossover = new PMXCrossover(0.9);
        PermutationSwapMutation mutation = new PermutationSwapMutation<>(1.0 / problem.getNumberOfVariables());
//        var selection = new BinaryTournamentSelection<>();

        // Configurar el algoritmo
        var algorithm = new NSGAIIBuilder<>(problem, crossover, mutation)
                .setMaxIterations(10000)
                .setPopulationSize(10)
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