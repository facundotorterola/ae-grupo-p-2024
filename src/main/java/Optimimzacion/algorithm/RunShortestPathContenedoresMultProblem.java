package Optimimzacion.algorithm;

import Optimimzacion.modelo.Camion;
import Optimimzacion.modelo.Contenedor;
import Optimimzacion.problem.ShortestPathMultCamionesProblem;
import Optimimzacion.utils.LeerCSV;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.operator.impl.crossover.PMXCrossover;
import org.uma.jmetal.operator.impl.mutation.PermutationSwapMutation;
import org.uma.jmetal.solution.PermutationSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.evaluator.impl.MultithreadedSolutionListEvaluator;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RunShortestPathContenedoresMultProblem {
    public static void main(String[] args) {
        // Definir constantes generales
        int CANTIDAD_CAMIONES = 27;
        int CANTIDAD_CONTENEDORES = 4185;

        // Obtener los contenedores desde el CSV
        Map<Integer, Contenedor> contenedores = LeerCSV.getContenedores(CANTIDAD_CONTENEDORES);
        int cantidadContenedores = contenedores.size();
        int cantidadContenedoresPorCamion = cantidadContenedores / CANTIDAD_CAMIONES;

        // Crear camiones con contenedores iniciales
        Map<Integer, Camion> camiones = new HashMap<>();
        for (int i = 0; i < CANTIDAD_CAMIONES; i++) {
            int idContenedor = cantidadContenedoresPorCamion * i;
            camiones.put(i, new Camion(i, contenedores.get(idContenedor)));
        }

        // Definir los valores de los parámetros a explorar
        int[] poblaciones = {50, 100, 200, 500};
        int[] generaciones = {50, 100, 200, 500};
        double[] greedyProbabilities = {0.1, 0.3, 0.5, 0.9};
        double[] probabilidadesCruce = {0.7, 0.8, 0.9, 1.0};
        double[] probabilidadesMutacion = {0.01, 0.05, 0.1, 0.2};

        // Variables para almacenar los mejores resultados globales
        PermutationSolution<Integer> mejorSolucionGlobal = null;
        double mejorDistanciaGlobal = Double.MAX_VALUE;

        // Explorar todas las combinaciones de parámetros
        for (int poblacion : poblaciones) {
            for (int generacion : generaciones) {
                for (double greedy : greedyProbabilities) {
                    for (double cruce : probabilidadesCruce) {
                        for (double mutacion : probabilidadesMutacion) {
                            System.out.println("Ejecutando con parámetros:");
                            System.out.println("Población: " + poblacion);
                            System.out.println("Generaciones: " + generacion);
                            System.out.println("Greedy: " + greedy);
                            System.out.println("Cruce: " + cruce);
                            System.out.println("Mutación: " + mutacion);

                            // Configurar el problema
                            ShortestPathMultCamionesProblem problem = new ShortestPathMultCamionesProblem(camiones, contenedores, greedy);

                            // Configurar operadores genéticos
                            var crossover = new PMXCrossover(cruce);
                            PermutationSwapMutation mutation = new PermutationSwapMutation<>(mutacion);

                            // Configurar el algoritmo NSGA-II
                            var algorithm = new NSGAIIBuilder<>(problem, crossover, mutation)
                                    .setPopulationSize(poblacion)
                                    .setMaxIterations(generacion)
                                    .build();

                            // Ejecutar el algoritmo
                            new AlgorithmRunner.Executor(algorithm).execute();

                            // Obtener los resultados de esta ejecución
                            List<PermutationSolution<Integer>> population = (List<PermutationSolution<Integer>>) algorithm.getResult();
                            PermutationSolution<Integer> mejorSolucionParametro = population.stream()
                                    .min(Comparator.comparingDouble(s -> s.getObjective(0)))
                                    .orElseThrow();

                            double distanciaMejorSolucion = mejorSolucionParametro.getObjective(0);

                            // Imprimir los resultados de la mejor solución para esta configuración
                            System.out.println("Mejor Solución para esta configuración:");
                            for (int i = 0; i < mejorSolucionParametro.getNumberOfVariables(); i++) {
                                System.out.print(mejorSolucionParametro.getVariableValue(i) + " -> ");
                            }
                            System.out.println("\nDistancia Total: " + distanciaMejorSolucion);
                            System.out.println("------------------------------------");

                            // Actualizar los mejores resultados globales si se encuentra una mejor solución
                            if (distanciaMejorSolucion < mejorDistanciaGlobal) {
                                mejorDistanciaGlobal = distanciaMejorSolucion;
                                mejorSolucionGlobal = mejorSolucionParametro;
                            }
                        }
                    }
                }
            }
        }

        // Mostrar los mejores resultados globales al final
        System.out.println("====================================");
        System.out.println("Mejor Solución Global:");
        if (mejorSolucionGlobal != null) {
            for (int i = 0; i < mejorSolucionGlobal.getNumberOfVariables(); i++) {
                System.out.print(mejorSolucionGlobal.getVariableValue(i) + " -> ");
            }
            System.out.println("\nDistancia Total: " + mejorDistanciaGlobal);
        }
        System.out.println("====================================");
    }
}