package Optimimzacion.modelo;

public class Posicion {

    private double latitud;
    private double longitud;

    public Posicion(double latitud, double longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }
    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public double calcularDistancia(Posicion posicion) {
        double x = this.latitud - posicion.getLatitud();
        double y = this.longitud - posicion.getLongitud();
        return Math.sqrt(x * x + y * y);
    }

    @Override
    public String toString() {
        return "Latitud: " + latitud + " Longitud: " + longitud;
    }

}
