package Optimimzacion.algorithm;


import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.operator.impl.crossover.SinglePointCrossover;
import org.uma.jmetal.operator.impl.mutation.BitFlipMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.problem.singleobjective.OneMax;
import org.uma.jmetal.solution.BinarySolution;
import  org.uma.jmetal.util.AlgorithmRunner;

import java.util.List;

public class RunOneMax {
    public static void main(String[] args) {
        int numberOfBits = 100;

        // Crear el problema OneMax
        Problem<BinarySolution> problem = new OneMax(numberOfBits);

        // Configurar los operadores
        var crossover = new SinglePointCrossover(0.9);
        var mutation = new BitFlipMutation(1.0 / numberOfBits);
        var selection = new BinaryTournamentSelection<>();

        // Configurar el algoritmo
        var algorithm = new NSGAIIBuilder<>(problem, crossover, mutation)
                .setMaxIterations(10000)
                .setPopulationSize(100)
                .build();

        // Ejecutar el algoritmo
        new AlgorithmRunner.Executor(algorithm).execute();

        // Mostrar resultados
        List<BinarySolution> population = algorithm.getResult();
        population.forEach(solution -> {
            System.out.println("Solution: " + solution.getVariableValueString(0) +
                    " Objective: " + -solution.getObjective(0));
        });
    }
}
