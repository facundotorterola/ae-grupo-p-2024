package Optimimzacion.modelo;

import java.util.ArrayList;
import java.util.List;

public class Camion {
    private int idCamion;
    private Contenedor contenedorActual;
    private List<Contenedor> contenedores;
    private final double capacidad;
    private double capacidadUtilizada;

    public Camion(int idCamion, Contenedor contenedorActual) {
        this.idCamion = idCamion;
        this.contenedorActual = contenedorActual;
        this.contenedores = new ArrayList<>();
        this.contenedores.add(contenedorActual);
        // Ubicacion inicial -34.849372, -56.095847
        this.capacidad = 10 *1000; // 10 toneladas
        this.capacidadUtilizada = contenedorActual.getDemanda();
    }

    public int getIdCamion() {
        return this.idCamion;
    }
    public Contenedor getContenedorActual() {
        return this.contenedorActual;
    }
    public List<Contenedor> getContenedores() {
        return this.contenedores;
    }

    public void setContenedorActual(Contenedor contenedorABuscar) {
        this.contenedorActual = contenedorABuscar;
    }

    public double getCapacidad() {
        return capacidad;
    }


    public double getCapacidadUtilizada() {
        return capacidadUtilizada;
    }

    public void setCapacidadUtilizada(double capacidadUtilizada) {
        this.capacidadUtilizada = capacidadUtilizada;
    }
}
