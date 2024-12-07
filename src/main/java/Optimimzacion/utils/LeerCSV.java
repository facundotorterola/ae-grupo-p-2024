package Optimimzacion.utils;

import Optimimzacion.modelo.Contenedor;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LeerCSV {
    public static Map<Integer, Contenedor> getContenedores() {
        String rutaCSV = "src/main/resources/levantes-contenedores.csv";
        // Mapa para almacenar los contenedores
        Map<Integer, Contenedor> contenedores = new HashMap<>();

        try (CSVReader reader = new CSVReader(new FileReader(rutaCSV))) {
            String[] linea;

            // Leer encabezados (opcional)
            reader.readNext();
            int counter = 0;
            // Leer cada línea del archivo
            while ((linea = reader.readNext()) != null) {
                double latitud = Double.parseDouble(linea[4]);
                double longitud = Double.parseDouble(linea[5]);

                // Crear y almacenar el contenedor
                Contenedor contenedor = new Contenedor(counter, latitud, longitud);
                contenedores.put(counter, contenedor);
                counter++;
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }

        return contenedores;
    }
}
