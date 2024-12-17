package Optimimzacion.problem;

import Optimimzacion.modelo.Camion;
import Optimimzacion.modelo.Contenedor;
import org.uma.jmetal.problem.impl.AbstractIntegerProblem;
import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.solution.impl.DefaultIntegerSolution;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class ShortestPathMultCamionesContenedoresProblem extends AbstractIntegerProblem {

    private final Map<Integer, Camion> camiones;
    private final Map<Integer, Contenedor> contenedores;
    private final int cantidadContenedores;
    private final int cantidadCamiones;
    private final double greedyProbability;

    public ShortestPathMultCamionesContenedoresProblem(Map<Integer, Camion> camiones, Map<Integer, Contenedor> contenedores, double greedyProbability) {
        this.camiones = camiones;
        this.contenedores = contenedores;
        this.cantidadContenedores = contenedores.size();
        this.cantidadCamiones = camiones.size();
        this.greedyProbability = greedyProbability;

        setNumberOfVariables(cantidadContenedores); // Número de contenedores
        setNumberOfObjectives(1);                   // Minimizar una métrica
        setLowerLimit(Collections.nCopies(cantidadContenedores, 1)); // Valor mínimo: Camión 1
        setUpperLimit(Collections.nCopies(cantidadContenedores, cantidadCamiones)); // Valor máximo: Camión N
        setName("ShortestPathMultCamiones");
    }

    @Override
    public void evaluate(IntegerSolution solution) {
        double totalCost = 0.0;
        double demandaScore = 0.0; // Para evaluar la prioridad de demandas tempranas
        double alpha = 1; // Coeficiente que ajusta la importancia de la demanda

        // Crear una copia de los camiones
        Map<Integer, Camion> camionesTemporales = crearCopiasCamionesMap();

        // Calcular la distancia total y la penalización de la demanda
        for (int i = 0; i < cantidadContenedores; i++) {
            Contenedor contenedor = contenedores.get(i);
            int idCamion = solution.getVariableValue(i);
            Camion camion = camionesTemporales.get(idCamion);

            double distancia = camion.getPosicionActual().calcularDistancia(contenedor.getPosicion());
            totalCost += distancia;

            // Actualizar la posición del camión
            camion.agregarContenedor(contenedor);

            // Aumentar el score para demandas atendidas tempranamente
            double demanda = contenedor.getDemandaNormalizada();
            demandaScore += demanda;
        }

        // Restar el score de demanda ponderado por alpha
        totalCost -= alpha * demandaScore;

        // El objetivo es minimizar la distancia total con prioridad de demanda
        solution.setObjective(0, totalCost);
    }

    @Override
    public IntegerSolution createSolution() {
        IntegerSolution solution = new DefaultIntegerSolution(this);

        // Crear asignaciones de contenedores a camiones
        for (int i = 0; i < cantidadContenedores; i++) {
            int idCamion = (Math.random() < greedyProbability)
                    ? seleccionarMejorCamion(contenedores.get(i)).getIdCamion()
                    : seleccionarCamionAleatorio().getIdCamion();
            solution.setVariableValue(i, idCamion);
        }

        return solution;
    }

    private Camion seleccionarMejorCamion(Contenedor contenedor) {
        return camiones.values().stream()
                .min(Comparator.comparingDouble(c -> c.getPosicionActual().calcularDistancia(contenedor.getPosicion())))
                .orElseThrow();
    }

    private Camion seleccionarCamionAleatorio() {
        int randomId = (int) (Math.random() * cantidadCamiones); // Camión 1 a N
        return camiones.get(randomId);
    }

    private Map<Integer, Camion> crearCopiasCamionesMap() {
        Map<Integer, Camion> copias = new HashMap<>();
        for (Camion camion : this.camiones.values()) {
            copias.put(camion.getIdCamion(), new Camion(camion.getIdCamion(), camion.getPosicionActual()));
        }
        return copias;
    }
}