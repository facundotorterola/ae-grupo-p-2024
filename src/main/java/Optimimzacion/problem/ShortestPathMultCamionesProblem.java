package Optimimzacion.problem;

import Optimimzacion.modelo.Camion;
import Optimimzacion.modelo.Contenedor;
import org.uma.jmetal.problem.impl.AbstractIntegerPermutationProblem;
import org.uma.jmetal.solution.PermutationSolution;

import java.util.*;

public class ShortestPathMultCamionesProblem extends AbstractIntegerPermutationProblem{
    private final Map<Integer,Camion> camiones;
    private final Map<Integer, Contenedor> contenedores;
    private  int cantidadContenedores;
    private double greedyProbability = 0.9;

    public ShortestPathMultCamionesProblem(int cantidadContenedores,Map<Integer,Camion> camiones, Map<Integer, Contenedor> contenedores) {
        this.camiones = camiones;
        this.contenedores = contenedores;
        this.cantidadContenedores = cantidadContenedores;
        setNumberOfVariables(cantidadContenedores); // Número de nodos
        setNumberOfObjectives(1);                      // Minimizar una sola métrica

        setName("ShortestPathMultCamiones");
    }
    @Override
    public int getPermutationLength() {
        return this.cantidadContenedores;
    }


    @Override
    public void evaluate(PermutationSolution<Integer> solution) {
        double totalDistance = 0;
        double demandaScore = 0;
        double alpha = 1.0/(cantidadContenedores/camiones.size());
        double beta = 1.0* camiones.size();

        // Crear copias temporales de los camiones
        List<Camion> camionesTemporales = crearCopiasCamiones();

        // Crear una lista temporal de contenedores basada en la solución
        List<Contenedor> contenedoresOrdenados = new ArrayList<>();
        for (int i = 0; i < solution.getNumberOfVariables(); i++) {
            contenedoresOrdenados.add(contenedores.get(solution.getVariableValue(i)));
        }

        // Calcular la distancia total y asignar los contenedores
        for (int i = 0; i < contenedoresOrdenados.size(); i++) {
            Contenedor contenedor = contenedoresOrdenados.get(i);
            Camion camion = camionesTemporales.get(i % camiones.size());

            // Calcular la distancia desde el contenedor actual al nuevo contenedor
            double distancia = camion.getContenedorActual().calcularDistancia(contenedor);
            totalDistance += beta * distancia;

            // Actualizar el estado del camión
            camion.getContenedores().add(contenedor);
            camion.setContenedorActual(contenedor);

            // Incrementar el score de demanda (mayor demanda en posiciones tempranas es mejor)
            double demanda = contenedor.getDemanda();
            demandaScore += demanda / (i + 1); // Penaliza posiciones tardías
        }

        // Ajustar el costo total con la ponderación de la demanda
        double totalCost =   totalDistance - alpha * demandaScore;

        // Asignar el costo total como el objetivo de la solución
        solution.setObjective(0, totalCost);

        // Loggear las asignaciones de contenedores a los camiones
//        loggearAsignaciones (camionesTemporales);
    }



    private List<Camion> crearCopiasCamiones() {
        List<Camion> copias = new ArrayList<>();

        for (Camion camion : camiones.values()) {
            Camion copia = new Camion(camion.getIdCamion(), camion.getContenedorActual());
//            copia.getContenedores().add(camion.getContenedorActual());
            copias.add(copia);
        }
        return copias;
    }

    private void loggearAsignaciones(List<Camion> camionesTemporales) {
        for (Camion camion : camionesTemporales) {
            System.out.println("Camión: " + camion.getIdCamion());
            System.out.print("Contenedores: ");
            for (Contenedor contenedor : camion.getContenedores()) {
                System.out.print(contenedor.getId() + " -> ");
            }
            System.out.println();
        }
    }

//    @Override
//    public PermutationSolution<Integer> createSolution() {
//        // Crear una solución vacía utilizando la estructura base del problema
//        PermutationSolution<Integer> solution = super.createSolution();
//
//        // Obtener una lista de contenedores desde las claves del mapa
//        List<Integer> listaContenedores = new ArrayList<>(contenedores.keySet());
//
//        // Mezclar aleatoriamente los contenedores para generar una solución única
//        Collections.shuffle(listaContenedores);
//
//        // Asignar los valores mezclados a las variables de la solución
//        for (int i = 0; i < cantidadContenedores; i++) {
//            solution.setVariableValue(i, listaContenedores.get(i));
//        }
////        System.out.println("Solution: " + solution);
//
//
//        return solution;
//    }

@Override
public PermutationSolution<Integer> createSolution() {
    // Generar un número aleatorio para decidir
    double randomValue = Math.random();

    // Si el valor aleatorio es menor que la probabilidad, usar greedy; de lo contrario, aleatorio
    if (randomValue < greedyProbability) {
        PermutationSolution<Integer> solution  = createGreedySolution();// Generar solución greedy
        return solution;
    } else {
        PermutationSolution<Integer> solution  = createRandomSolution(); // Generar solución completamente aleatoria
        return solution;
    }
}

    // Solución greedy (como se describió anteriormente)
    private PermutationSolution<Integer> createGreedySolution() {
        PermutationSolution<Integer> solution = super.createSolution();

        // Crear una lista de contenedores ordenada por demanda (mayor a menor)
        List<Map.Entry<Integer, Contenedor>> contenedoresOrdenados = new ArrayList<>(contenedores.entrySet());
        contenedoresOrdenados.sort((entry1, entry2) ->
                Double.compare(entry2.getValue().getDemanda(), entry1.getValue().getDemanda()));

        // Asignar los contenedores a los camiones de manera greedy
        int contenedorActual = 0;
        for (Map.Entry<Integer, Contenedor> entry : contenedoresOrdenados) {
            Contenedor contenedor = entry.getValue();
            solution.setVariableValue(contenedorActual, contenedor.getId());
            contenedorActual++;
        }

        return solution;
    }

    // Solución completamente aleatoria
    private PermutationSolution<Integer> createRandomSolution() {
        PermutationSolution<Integer> solution = super.createSolution();

        // Crear una lista aleatoria de IDs de contenedores
        List<Integer> contenedoresAleatorios = new ArrayList<>(contenedores.keySet());
        Collections.shuffle(contenedoresAleatorios);

        // Asignar los valores a la solución
        for (int i = 0; i < contenedoresAleatorios.size(); i++) {
            solution.setVariableValue(i, contenedoresAleatorios.get(i));
        }

        return solution;
    }

    private Camion seleccionarMejorCamion(List<Camion> camiones, Contenedor contenedor) {
        return camiones.stream()
                .min((c1, c2) -> Double.compare(
                        c1.getContenedorActual().calcularDistancia(contenedor),
                        c2.getContenedorActual().calcularDistancia(contenedor)
                ))
                .orElseThrow();
    }

}
