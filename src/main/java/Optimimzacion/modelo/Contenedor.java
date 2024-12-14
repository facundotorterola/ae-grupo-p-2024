package Optimimzacion.modelo;


public class Contenedor {
    private int id;
    private double latitud;
    private double longitud;
    private int demanda;
    private double demandaNormalizada;
    private double capacidad;
    private double capacidadUtilizada;

    public Contenedor(int id, double latitud, double longitud, int demanda, double demandaNormalizada) {
        this.id = id;
        this.latitud = latitud;
        this.longitud = longitud;
        this.demanda = demanda;
        this.demandaNormalizada = demandaNormalizada;
        this.capacidad = 3.2 * 130;
        this.capacidadUtilizada = demanda;
    }

    public int getId() {
        return id;
    }

    public double getLatitud() {
        return latitud;
    }

    public int getDemanda() {
        return demanda;
    }

    public double getLongitud() {
        return longitud;
    }

    public double getDemandaNormalizada() {return demandaNormalizada;}

    public double calcularDistancia(Contenedor contenedor) {
        double x = this.latitud - contenedor.getLatitud();
        double y = this.longitud - contenedor.getLongitud();
        return Math.sqrt(x * x + y * y);
    }


    @Override
    public String toString() {
        return "Contenedor: " + id + " lat: " + latitud + " ,lng: " + longitud + " demanda: " + demanda;
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
}

