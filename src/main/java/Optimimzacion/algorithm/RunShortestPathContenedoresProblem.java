package Optimimzacion.algorithm;

import Optimimzacion.modelo.Contenedor;
import Optimimzacion.utils.LeerCSV;

import java.util.Map;


public class RunShortestPathContenedoresProblem {



    public static void main(String[] args) {
        int CANTIDAD_CAMIONES = 3;

        // Obtener los contenedores desde el CSV
        Map<Integer, Contenedor> contenedores = LeerCSV.getContenedores(0);
        int cantidadContenedores = contenedores.size();
//
//        // Mapa para almacenar los recorridos de cada camión
//        Map<Integer, RecorridoCamiones> recorridoCamiones = new HashMap<>();
//
//        // Calcular la cantidad de contenedores por camión
//        int cantidadContenedoresPorCamion = cantidadContenedores / CANTIDAD_CAMIONES;
//        System.out.println("Cantidad de contenedores por camión: " + cantidadContenedoresPorCamion);
//
//        // Dividir contenedores entre camiones
//        int indiceContenedor = 0;
//        for (int camionActual = 1; camionActual <= CANTIDAD_CAMIONES; camionActual++) {
//            Map<Integer, Contenedor> contenedoresCamion = new HashMap<>();
//
//            // Asignar contenedores al camión actual
//            for (int j = 0; j < cantidadContenedoresPorCamion && indiceContenedor < cantidadContenedores; j++) {
//                Contenedor contenedor = contenedores.get(indiceContenedor);
//                contenedoresCamion.put(indiceContenedor, contenedor);
//                System.out.println("Asignando Contenedor " + indiceContenedor + " al Camión " + camionActual);
//                indiceContenedor++;
//            }
//
//            // Crear un recorrido para el camión actual
//            RecorridoCamiones recorridoCamion = new RecorridoCamiones(camionActual, contenedoresCamion);
//            recorridoCamiones.put(camionActual, recorridoCamion);
//        }
//
//        // Si hay contenedores sobrantes, asignarlos al último camión
//        if (indiceContenedor < cantidadContenedores) {
//            Map<Integer, Contenedor> contenedoresRestantes = new HashMap<>();
//            while (indiceContenedor < cantidadContenedores) {
//                Contenedor contenedor = contenedores.get(indiceContenedor);
//                contenedoresRestantes.put(indiceContenedor, contenedor);
//                System.out.println("Asignando Contenedor Sobrante " + indiceContenedor + " al Último Camión");
//                indiceContenedor++;
//            }
//
//            // Añadir los contenedores sobrantes al último camión
//            RecorridoCamiones ultimoRecorrido = recorridoCamiones.get(CANTIDAD_CAMIONES);
//            ultimoRecorrido.getContenedoresCamion().putAll(contenedoresRestantes);
//        }
//
//        // Imprimir el resumen de las asignaciones
//        for (Map.Entry<Integer, RecorridoCamiones> entry : recorridoCamiones.entrySet()) {
//            System.out.println("Camión " + entry.getKey() + ": " + entry.getValue().getContenedoresCamion().size() + " contenedores asignados.");
//        }
//
//
//        var problem = new ShortestPathProblemContenedores(recorridoCamiones, cantidadContenedoresPorCamion);
//
//        var crossover = new PMXCrossover(0.9);
//        PermutationSwapMutation mutation = new PermutationSwapMutation<>(1.0 / problem.getNumberOfVariables());
////        var selection = new BinaryTournamentSelection<>();
//
//        // Configurar el algoritmo
//        var algorithm = new NSGAIIBuilder<>(problem, crossover, mutation)
//                .setMaxIterations(10)
//                .setPopulationSize(10)
//                .build();
//
//        // Ejecutar el algoritmo
//        new AlgorithmRunner.Executor(algorithm).execute();
//
//        // Mostrar resultados
//        List<PermutationSolution<Integer>> population = (List<PermutationSolution<Integer>>) algorithm.getResult();
//        population.forEach(solution -> {
//            System.out.println("Solution: " + solution.getVariableValueString(0) +
//                    " Objective: " + -solution.getObjective(0));
//        });
////        PermutationSolution<Integer> bestSolution = population.get(0);
////        for (PermutationSolution<Integer> solution : population) {
////            if (solution.getObjective(0) < bestSolution.getObjective(0)) {
////                bestSolution = solution;
////            }
////
////        }
////        System.out.println("====================================");
////        System.out.print("Solution: [ ");
////        for (int i = 0; i < bestSolution.getNumberOfVariables(); i++) {
////            System.out.print(bestSolution.getVariableValue(i) + " ");
////        }
////        System.out.println("]");
////        System.out.println("Total Distance: " + bestSolution.getObjective(0));
////        System.out.println("====================================");
    }


//    }

//        GrafoContenedores grafoContenedores = new GrafoContenedores(contenedores);
//        // Crear el problema
//        ShortestPathProblemContenedores problem = new ShortestPathProblemContenedores(grafoContenedores);
//
//        // Operadores genéticos
}