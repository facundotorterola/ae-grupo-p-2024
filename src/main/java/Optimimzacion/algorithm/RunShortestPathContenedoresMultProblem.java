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
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
public class RunShortestPathContenedoresMultProblem {


    public static void main(String[] args) throws IOException {
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

        // Archivo para guardar los resultados
        FileWriter csvWriter = new FileWriter("resultados.csv");
        csvWriter.append("Poblacion,Generaciones,Greedy,Cruce,Mutacion,Fitness\n");

        // AtomicInteger para identificar las combinaciones de parámetros
        AtomicInteger combinacionId = new AtomicInteger(0);

        // Explorar todas las combinaciones de parámetros en paralelo
        IntStream.of(poblaciones).parallel().forEach(poblacion -> {
            IntStream.of(generaciones).parallel().forEach(generacion -> {
                DoubleStream.of(greedyProbabilities).parallel().forEach(greedy -> {
                    DoubleStream.of(probabilidadesCruce).parallel().forEach(cruce -> {
                        DoubleStream.of(probabilidadesMutacion).parallel().forEach(mutacion -> {
                            int id = combinacionId.incrementAndGet();

                            System.out.println("Ejecutando combinación " + id + " con parámetros:");
                            System.out.println("Población: " + poblacion + ", Generaciones: " + generacion +
                                    ", Greedy: " + greedy + ", Cruce: " + cruce + ", Mutación: " + mutacion);

                            // Configurar el problema
                            ShortestPathMultCamionesProblem problem = new ShortestPathMultCamionesProblem(camiones, contenedores, greedy);

                            // Configurar operadores genéticos
                            var crossover = new PMXCrossover(cruce);
                            PermutationSwapMutation mutationOperator = new PermutationSwapMutation<>(mutacion);

                            // Configurar el algoritmo NSGA-II
                            var algorithm = new NSGAIIBuilder<>(problem, crossover, mutationOperator)
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

                            // Guardar los resultados en el archivo CSV
                            synchronized (csvWriter) {
                                try {
                                    csvWriter.append(String.format("%d,%d,%.2f,%.2f,%.2f,%.2f\n",
                                            poblacion, generacion, greedy, cruce, mutacion, distanciaMejorSolucion));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            System.out.println("Combinación " + id + " completada.");
                        });
                    });
                });
            });
        });

        // Cerrar el archivo CSV
        csvWriter.close();

        System.out.println("====================================");
        System.out.println("Ejecución completada. Resultados guardados en 'resultados.csv'.");
        System.out.println("====================================");
    }
}