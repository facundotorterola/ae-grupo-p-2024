package Optimimzacion.modelo;


public class Contenedor {
    private int id;
    private double latitud;
    private double longitud;
    private int demanda;

    public Contenedor(int id, double latitud, double longitud, int demanda) {
        this.id = id;
        this.latitud = latitud;
        this.longitud = longitud;
        this.demanda = demanda;
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

    public double calcularDistancia(Contenedor contenedor) {
        double x = this.latitud - contenedor.getLatitud();
        double y = this.longitud - contenedor.getLongitud();
        return Math.sqrt(x * x + y * y);
    }


    @Override
    public String toString() {
        return "Contenedor: " + id + " lat: " + latitud + " ,lng: " + longitud + " demanda: " + demanda;
    }
}

