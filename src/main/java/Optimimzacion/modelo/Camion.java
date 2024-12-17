package Optimimzacion.modelo;

import java.util.ArrayList;
import java.util.List;

public class Camion {
    private int idCamion;
    private Posicion posicionActual;
    private List<Contenedor> contenedores;
    private final double capacidad;
    private double capacidadUtilizada;

    public Camion(int idCamion, Posicion posicionActual) {
        this.idCamion = idCamion;
        this.posicionActual = posicionActual;
        this.contenedores = new ArrayList<>();
//        this.contenedores.add(contenedorActual);
//        // Ubicacion inicial -34.849372, -56.095847
        this.capacidad = 10 *1000; // 10 toneladas
        this.capacidadUtilizada = 0;
    }

    public int getIdCamion() {
        return this.idCamion;
    }

    public List<Contenedor> getContenedores() {
        return this.contenedores;
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

    public Posicion getPosicionActual() {
        return posicionActual;
    }

    public void setPosicionActual(Posicion posicionActual) {
        this.posicionActual = posicionActual;
    }

    public void agregarContenedor(Contenedor contenedor) {
        this.contenedores.add(contenedor);
        this.capacidadUtilizada += contenedor.getDemanda();
        if (this.capacidadUtilizada > this.capacidad) {
            this.capacidadUtilizada = 0;
            this.posicionActual = new Posicion(-34.849372, -56.095847);
        }else {
            this.posicionActual = contenedor.getPosicion();
        }

    }
}
