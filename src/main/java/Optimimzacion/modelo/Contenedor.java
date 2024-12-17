package Optimimzacion.modelo;


public class Contenedor {
    private int id;
    private Posicion posicion;
    private int demanda;
    private double demandaNormalizada;
    private double capacidad;
    private double capacidadUtilizada;

    public Contenedor(int id, Posicion posicion, int demanda, double demandaNormalizada) {
        this.id = id;
        this.posicion = posicion;
        this.demanda = demanda;
        this.demandaNormalizada = demandaNormalizada;
        this.capacidad = 3.2 * 130;
        this.capacidadUtilizada = demanda;
    }

    public int getId() {
        return id;
    }



    public int getDemanda() {
        return demanda;
    }



    public double getDemandaNormalizada() {return demandaNormalizada;}



    @Override
    public String toString() {
        return "Contenedor: " + id + " posicion " + posicion + " demanda: " + demanda;
    }

    public double getCapacidadUtilizada() {
        return capacidadUtilizada;
    }

    public void setCapacidadUtilizada(double capacidadUtilizada) {
        this.capacidadUtilizada = capacidadUtilizada;
    }

    public double getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(double capacidad) {
        this.capacidad = capacidad;
    }


    public Posicion getPosicion() {
        return posicion;
    }

    public void setPosicion(Posicion posicion) {
        this.posicion = posicion;
    }
}

