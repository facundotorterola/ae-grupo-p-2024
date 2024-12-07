package Optimimzacion.modelo;


public class Contenedor {
    private int id;
    private double latitud;
    private double longitud;

    public Contenedor(int id, double latitud, double longitud) {
        this.id = id;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public int getId() {
        return id;
    }

    public double getLatitud() {
        return latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    @Override
    public String toString() {
        return "Contenedor: " + id + " lat: " + latitud + " ,lng: " + longitud;
    }
}

