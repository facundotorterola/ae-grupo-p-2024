package Optimimzacion.modelo;

public class Deposito {

    private int id;
    private Posicion posicion;

    public Deposito(int id, Posicion posicion) {
        this.id = id;
        this.posicion = posicion;
    }

    public Posicion getPosicion() {
        return posicion;
    }

    public void setPosicion(Posicion posicion) {
        this.posicion = posicion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
