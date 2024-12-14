package Optimimzacion.utils;

import Optimimzacion.modelo.Contenedor;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LeerCSV {
    public static Map<Integer, Contenedor> getContenedores(int cantidadContenedores) {
        String rutaCSV = "src/main/resources/contenedoresFiltrados.csv";
        // Mapa para almacenar los contenedores
        Map<Integer, Contenedor> contenedores = new HashMap<>();

        try (CSVReader reader = new CSVReader(new FileReader(rutaCSV))) {
            String[] linea;

            // Leer encabezados (opcional)
            reader.readNext();
            int counter = 0;
            // Leer cada línea del archivo
            while ((linea = reader.readNext()) != null) {
                double longitud = Double.parseDouble(linea[0]);
                double latitud = Double.parseDouble(linea[1]);
                int demanda = Integer.parseInt(linea[2]);
                double demanda_normalizada = Double.parseDouble(linea[3]);
                int id = Integer.parseInt(linea[4]);


                // Crear y almacenar el contenedor
                Contenedor contenedor = new Contenedor(id, latitud, longitud, demanda, demanda_normalizada);
                contenedores.put(counter, contenedor);
                counter++;
                if (counter == cantidadContenedores) {
                    break;
                }
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }

        return contenedores;
    }
}
