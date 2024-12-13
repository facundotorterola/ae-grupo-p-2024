package Optimimzacion.modelo;

import java.util.ArrayList;
import java.util.List;

public class Camion {
    private int idCamion;
    private Contenedor contenedorActual;
    private List<Contenedor> contenedores;

    public Camion(int idCamion, Contenedor contenedorActual) {
        this.idCamion = idCamion;
        this.contenedorActual = contenedorActual;
        this.contenedores = new ArrayList<>();
        this.contenedores.add(contenedorActual);
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
}
