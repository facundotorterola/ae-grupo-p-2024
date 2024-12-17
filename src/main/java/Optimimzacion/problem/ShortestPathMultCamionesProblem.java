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
    private double greedyProbability;

    public ShortestPathMultCamionesProblem(Map<Integer,Camion> camiones, Map<Integer, Contenedor> contenedores, double greedyProbability) {
        this.camiones = camiones;
        this.contenedores = contenedores;
        this.cantidadContenedores = contenedores.size();
        this.greedyProbability = greedyProbability;
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
        double alpha = 1.0/(camiones.size());
        double beta = 1.0 / camiones.size();

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
            double distancia = camion.getPosicionActual().calcularDistancia(contenedor.getPosicion());
            totalDistance += beta * distancia;

            // Actualizar el estado del camión
            camion.agregarContenedor(contenedor);
            // Incrementar el score de demanda (mayor demanda en posiciones tempranas es mejor)
            double demanda = contenedor.getDemandaNormalizada();
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
            Camion copia = new Camion(camion.getIdCamion(), camion.getPosicionActual());
//            copia.getContenedores().add(camion.getContenedorActual());
            copias.add(copia);
        }
        return copias;
    }

    private Map<Integer, Camion> crearCopiasCamionesMap() {
        Map<Integer, Camion> copias = new HashMap<>();

        for (Camion camion : this.camiones.values()) {
            Camion copia = new Camion(camion.getIdCamion(), camion.getPosicionActual());
            copias.put(camion.getIdCamion(), copia);
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

    @Override
    public PermutationSolution<Integer> createSolution() {
        // Crear una solución vacía
        PermutationSolution<Integer> solution = super.createSolution();

        // Crear una lista de contenedores sin visitar
        List<Contenedor> contenedoresSinVisitar = new ArrayList<>(contenedores.values());

        // Crear copias temporales de los camiones para manipular su estado
        List<Camion> camionesTemporales = crearCopiasCamiones();

        // Generar la solución contenedor por contenedor
        for (int i = 0; i < solution.getNumberOfVariables(); i++) {
            // Seleccionar el camión actual basado en el índice
            Camion camionActual = camionesTemporales.get(i % camionesTemporales.size());

            // Tomar decisión: greedy o aleatorio
            double randomValue = Math.random();
            Contenedor contenedorSeleccionado;
            if (randomValue < greedyProbability) {
                // Estrategia greedy: Seleccionar el mejor contenedor
                contenedorSeleccionado = obtenerMejorContenedorGreedy(camionActual, contenedoresSinVisitar);
            } else {
                // Estrategia aleatoria: Seleccionar un contenedor aleatorio
                contenedorSeleccionado = obtenerContenedorAleatorio(contenedoresSinVisitar);
            }

            // Asignar el contenedor a la solución
            solution.setVariableValue(i, contenedorSeleccionado.getId());

            // Actualizar el estado del camión
            camionActual.agregarContenedor(contenedorSeleccionado);

            // Eliminar el contenedor de los sin visitar
            contenedoresSinVisitar.remove(contenedorSeleccionado);
        }

        return solution;
    }


    // Método para seleccionar el mejor contenedor basado en distancia o demanda
    private Camion obtenerMejorCamionGreedy(Contenedor contenedor) {
        double decisionThreshold = 0.5; // Probabilidad de seleccionar por demanda en lugar de distancia
        double randomValue = Math.random();

        if (randomValue < decisionThreshold) {
            // Seleccionar el contenedor con mayor demanda
            return camiones.values().stream()
                    .min(Comparator.comparingDouble(Camion::getCapacidadUtilizada))
                    .orElseThrow(() -> new RuntimeException("No hay contenedores disponibles para seleccionar."));
        } else {
            // Seleccionar el contenedor más cercano (por distancia)
            return camiones.values().stream()
                    .min(Comparator.comparingDouble(c -> contenedor.getPosicion().calcularDistancia(c.getPosicionActual())))
                    .orElseThrow(() -> new RuntimeException("No hay contenedores disponibles para seleccionar."));
        }
    }

    // Método para seleccionar un contenedor aleatorio
    private Contenedor obtenerContenedorAleatorio(List<Contenedor> contenedoresSinVisitar) {
        int randomIndex = (int) (Math.random() * contenedoresSinVisitar.size());
        return contenedoresSinVisitar.get(randomIndex);
    }


    private Camion obtenerCamionAleatorio() {
        int randomIndex = (int) (Math.random() * camiones.size());
        return camiones.get(randomIndex);
    }


    // Solución greedy (como se describió anteriormente)
    private PermutationSolution<Integer> createGreedySolution() {
        PermutationSolution<Integer> solution = super.createSolution();

        // Crear una lista de contenedores ordenada por demanda (mayor a menor)
        List<Map.Entry<Integer, Contenedor>> contenedoresOrdenados = new ArrayList<>(contenedores.entrySet());
        contenedoresOrdenados.sort((entry1, entry2) ->
                Double.compare(entry2.getValue().getDemanda(), entry1.getValue().getDemanda()));

        // Asignar los contenedores a los camiones de manera greedy
//        int contenedorActual = 0;
        List<Camion> copiasCamiones = crearCopiasCamiones();

        for (Map.Entry<Integer, Contenedor> entry : contenedoresOrdenados) {
            Contenedor contenedor = entry.getValue();
            Camion camion = seleccionarMejorCamion(copiasCamiones, contenedor);
            System.out.println("Camion: " + camion.getIdCamion() + " Contenedor: " + contenedor.getId());
            camion.getContenedores().add(contenedor);
            camion.setPosicionActual(contenedor.getPosicion());
        }

        int counter = 0;
        for(Camion camion: copiasCamiones){
            for (Contenedor contenedor : camion.getContenedores()) {
                solution.setVariableValue(counter, contenedor.getId());
            }
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
                        c1.getPosicionActual().calcularDistancia(contenedor.getPosicion()),
                        c2.getPosicionActual().calcularDistancia(contenedor.getPosicion())
                ))
                .orElseThrow();
    }


    private Contenedor obtenerMejorContenedorGreedy(Camion camion, List<Contenedor> contenedoresSinVisitar) {
        double decisionThreshold = 0.5; // Probabilidad de seleccionar por demanda en lugar de distancia
        double randomValue = Math.random();

        if (randomValue < decisionThreshold) {
            // Seleccionar el contenedor con mayor demanda
            return contenedoresSinVisitar.stream()
                    .max(Comparator.comparingDouble(Contenedor::getDemandaNormalizada))
                    .orElseThrow(() -> new RuntimeException("No hay contenedores disponibles para seleccionar."));
        } else {
            // Seleccionar el contenedor más cercano (por distancia)
            return contenedoresSinVisitar.stream()
                    .min(Comparator.comparingDouble(c -> camion.getPosicionActual().calcularDistancia(c.getPosicion())))
                    .orElseThrow(() -> new RuntimeException("No hay contenedores disponibles para seleccionar."));
        }
    }



}
