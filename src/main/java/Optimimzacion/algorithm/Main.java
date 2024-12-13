//package Optimimzacion.algorithm;
//
//import Optimimzacion.modelo.Contenedor;
//import Optimimzacion.modelo.RecorridoCamiones;
//import Optimimzacion.problem.ShortestPathProblemContenedores;
//import Optimimzacion.utils.LeerCSV;
//import org.uma.jmetal.solution.PermutationSolution;
//import org.uma.jmetal.util.AlgorithmRunner;
//
//import java.util.*;
//import java.util.logging.Logger;
//
//public class Main {
//    private static final Logger logger = Logger.getLogger(Main.class.getName());
//    private static final int CANTIDAD_CAMIONES = 3;
//
//    public static void main(String[] args) {
//        // Obtener los contenedores desde el CSV
//        Map<Integer, Contenedor> contenedores = LeerCSV.getContenedores();
//
//        // Validar datos de entrada
//        validarDatosEntrada(contenedores, CANTIDAD_CAMIONES);
//
//        // Dividir los contenedores entre los camiones
//        Map<Integer, RecorridoCamiones> recorridoCamiones = dividirContenedores(contenedores, CANTIDAD_CAMIONES);
//
//        // Imprimir resumen de asignaciones
//        imprimirResumen(recorridoCamiones);
//
//        // Crear el problema de optimización
//        int cantidadContenedoresPorCamion = contenedores.size() / CANTIDAD_CAMIONES;
//        var problem = new ShortestPathProblemContenedores(recorridoCamiones, cantidadContenedoresPorCamion);
//
//        // Configurar el algoritmo
//        var algorithm = this.configurarAlgoritmo(problem);
//
//        // Ejecutar el algoritmo
//        new AlgorithmRunner.Executor(algorithm).execute();
//        List<PermutationSolution<Integer>> population = algorithm.getResult();
//
//        // Mostrar los resultados
//        mostrarResultados(population);
//    }
//
//    /**
//     * Valida los datos de entrada.
//     */
//    private static void validarDatosEntrada(Map<Integer, Contenedor> contenedores, int cantidadCamiones) {
//        if (contenedores.isEmpty()) {
//            throw new IllegalArgumentException("No se encontraron contenedores en el archivo CSV.");
//        }
//
//        if (cantidadCamiones <= 0) {
//            throw new IllegalArgumentException("La cantidad de camiones debe ser mayor a 0.");
//        }
//
//        logger.info("Datos de entrada validados correctamente.");
//    }
//
//    /**
//     * Divide los contenedores entre los camiones.
//     */
//    private static Map<Integer, RecorridoCamiones> dividirContenedores(Map<Integer, Contenedor> contenedores, int cantidadCamiones) {
//        Map<Integer, RecorridoCamiones> recorridoCamiones = new HashMap<>();
//        int cantidadContenedores = contenedores.size();
//        int cantidadContenedoresPorCamion = cantidadContenedores / cantidadCamiones;
//        int contenedorActual = 0;
//
//        // Asignar contenedores a cada camión
//        for (int camion = 1; camion <= cantidadCamiones; camion++) {
//            Map<Integer, Contenedor> contenedoresCamion = new HashMap<>();
//
//            for (int i = 0; i < cantidadContenedoresPorCamion && contenedorActual < cantidadContenedores; i++) {
//                contenedoresCamion.put(contenedorActual, contenedores.get(contenedorActual));
//                contenedorActual++;
//            }
//
//            recorridoCamiones.put(camion, new RecorridoCamiones(camion, contenedoresCamion));
//        }
//
//        // Manejar contenedores sobrantes
//        if (contenedorActual < cantidadContenedores) {
//            Map<Integer, Contenedor> contenedoresRestantes = new HashMap<>();
//            while (contenedorActual < cantidadContenedores) {
//                contenedoresRestantes.put(contenedorActual, contenedores.get(contenedorActual));
//                contenedorActual++;
//            }
//            recorridoCamiones.get(cantidadCamiones).getContenedoresCamion().putAll(contenedoresRestantes);
//        }
//
//        logger.info("Contenedores divididos entre camiones.");
//        return recorridoCamiones;
//    }
//
//    /**
//     * Configura el algoritmo NSGA-II.
//     */
//    private static Algorithm<List<PermutationSolution<Integer>>> configurarAlgoritmo(ShortestPathProblemContenedores problem) {
//        var crossover = new PMXCrossover(0.9);
//        PermutationSwapMutation mutation = new PermutationSwapMutation<>(1.0 / problem.getNumberOfVariables());
//
//        return new NSGAIIBuilder<>(problem, crossover, mutation)
//                .setMaxIterations(10000)
//                .setPopulationSize(100)
//                .build();
//    }
//
//    /**
//     * Imprime un resumen de las asignaciones de contenedores.
//     */
//    private static void imprimirResumen(Map<Integer, RecorridoCamiones> recorridoCamiones) {
//        for (Map.Entry<Integer, RecorridoCamiones> entry : recorridoCamiones.entrySet()) {
//            logger.info("Camión " + entry.getKey() + ": " + entry.getValue().getContenedoresCamion().size() + " contenedores asignados.");
//        }
//    }
//
//    /**
//     * Muestra los resultados del algoritmo.
//     */
//    private static void mostrarResultados(List<PermutationSolution<Integer>> population) {
//        // Obtener la mejor solución del frente de Pareto
//        PermutationSolution<Integer> bestSolution = population.stream()
//                .min(Comparator.comparingDouble(solution -> solution.getObjective(0)))
//                .orElseThrow();
//
//        System.out.println("====================================");
//        System.out.print("Mejor Solución: [ ");
//        for (int i = 0; i < bestSolution.getNumberOfVariables(); i++) {
//            System.out.print(bestSolution.getVariableValue(i) + " ");
//        }
//        System.out.println("]");
//        System.out.println("Distancia Total: " + bestSolution.getObjective(0));
//        System.out.println("====================================");
//
//        // Mostrar todo el frente de Pareto
//        System.out.println("Frente de Pareto:");
//        for (PermutationSolution<Integer> solution : population) {
//            System.out.println("Solución: [ " +
//                    solution.getVariableValueString() +
//                    " ] Objetivo 1: " + solution.getObjective(0));
//        }
//    }
//}