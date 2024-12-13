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
        // Definir constantes
        int CANTIDAD_CAMIONES = 27;
        int POBLACION_INICIAL = 100; // Población más grande para mayor diversidad
        int MAX_GENERACIONES = 200;
        int CANTIDAD_CONTENEDORES = 1000;// Reducido para criterios de convergencia más dinámicos
        double PROB_CRUCE = 0.9;
        double PROB_MUTACION = 0.1;

        // Obtener los contenedores desde el CSV
        Map<Integer, Contenedor> contenedores = LeerCSV.getContenedores(CANTIDAD_CONTENEDORES);
        int cantidadContenedores = contenedores.size();
        int cantidadContenedoresPorCamion = cantidadContenedores / CANTIDAD_CAMIONES;
        Map<Integer, Camion> camiones = new HashMap<>();
        for (int i = 0; i < CANTIDAD_CAMIONES; i++) {
            int idContenedor = cantidadContenedoresPorCamion * i;
            camiones.put(i, new Camion(i, contenedores.get(idContenedor)));
            System.out.println("Camion: " + i + " Contenedor inicial: " + contenedores.get(idContenedor).getId());
        }

        // Problema y operadores genéticos
        ShortestPathMultCamionesProblem problem = new ShortestPathMultCamionesProblem(cantidadContenedores, camiones, contenedores);

        var crossover = new PMXCrossover(PROB_CRUCE);
        PermutationSwapMutation mutation = new PermutationSwapMutation<>(PROB_MUTACION);

        // Configurar el algoritmo NSGA-II
        var algorithm = new NSGAIIBuilder<>(problem, crossover, mutation)
                .setPopulationSize(POBLACION_INICIAL)
                .setMaxIterations(MAX_GENERACIONES)
//                .setEvaluator(new MultithreadedSolutionListEvaluator(2, problem)) // Paralelización
                .build();

        // Ejecutar el algoritmo
        new AlgorithmRunner.Executor(algorithm).execute();


//        for (int generation = 0; generation < algorithm.getPopulation().size(); generation++) {
//            // Log el fitness promedio, mejor fitness, etc.
////            System.out.println("Generation " + generation + " Best fitness: " + algorithm.getPopulation().get(generation).getObjective(0));
//        }
        // Mostrar resultados
        List<PermutationSolution<Integer>> population = (List<PermutationSolution<Integer>>) algorithm.getResult();
        PermutationSolution<Integer> bestSolution = population.stream()
                .min(Comparator.comparingDouble(s -> s.getObjective(0)))
                .orElseThrow();

        System.out.println("====================================");
        System.out.println("Mejor Solución: [ ");
        Set<Integer> contenedoresVisitados = new HashSet<>();
        for (Camion camion : camiones.values()) {
            contenedoresVisitados.add(camion.getContenedorActual().getId());
        }
        for (int i = 0; i < bestSolution.getNumberOfVariables(); i++) {
            Camion camion = camiones.get(i % camiones.size());
            Contenedor contenedor = contenedores.get(bestSolution.getVariableValue(i));
            System.out.println("Camion: " + camion.getIdCamion());
            System.out.println("Contenedor: " + contenedor.getId());
            if (!contenedoresVisitados.contains(contenedor.getId())) {
                camion.getContenedores().add(contenedor);
                camion.setContenedorActual(contenedor);
                contenedoresVisitados.add(contenedor.getId());
            } else {
                System.out.println("Contenedor ya visitado");
            }
        }

        // Mostrar contenedores por camión
        System.out.println("==============================");
        System.out.println(bestSolution);
        System.out.println("Contenedores por Camión: ");
        for (int i = 0; i < camiones.size(); i++) {
            Camion camion = camiones.get(i);
            System.out.println("Camion: " + camion.getIdCamion());
            System.out.print("Contenedores: ");
            camion.getContenedores().forEach(contenedor -> System.out.print(contenedor.getId() + " -> "));
            System.out.println();
        }
        System.out.println("]");

        System.out.println("Distancia Total: " + bestSolution.getObjective(0));
        System.out.println("====================================");
    }
}