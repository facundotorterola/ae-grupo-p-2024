package Optimimzacion.modelo;

import java.util.Map;

public class RecorridoCamiones {

    private int idCamion;
    private Map<Integer, Contenedor> contenedoresCamion;
    private GrafoContenedores grafoContenedores;

    public RecorridoCamiones(int idCamion, Map<Integer, Contenedor> contenedoresCamion) {
        this.idCamion = idCamion;
        this.contenedoresCamion = contenedoresCamion;
        this.grafoContenedores = new GrafoContenedores(contenedoresCamion);
    }

    public int getIdCamion() {
        return this.idCamion;
    }

    public Map<Integer, Contenedor> getContenedoresCamion() {
        return contenedoresCamion;
    }
    public GrafoContenedores getGrafoContenedores() {
        return this.grafoContenedores;
    }
}
