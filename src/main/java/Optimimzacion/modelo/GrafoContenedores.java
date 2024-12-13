package Optimimzacion.modelo;

import Optimimzacion.utils.LeerCSV;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.Comparator;
import java.util.Map;
import java.util.PriorityQueue;


public class GrafoContenedores {
    private DefaultUndirectedWeightedGraph<Integer, DefaultWeightedEdge> grafo;
    private Map<Integer, Contenedor> contenedores;

    public GrafoContenedores(Map<Integer,Contenedor> contenedores){
        int CANTIDAD_DE_CONEXIONES = 5;
        this.contenedores = contenedores;
        // Crear el grafo
        DefaultUndirectedWeightedGraph<Integer, DefaultWeightedEdge> grafo =
                new DefaultUndirectedWeightedGraph<>(DefaultWeightedEdge.class);

        // Agregar nodos (contenedores)
//        System.out.println("Nodos: " + contenedores.size());
//        System.out.println("Aristas: " + contenedores.values());
        for (Contenedor contenedor : contenedores.values()) {
            grafo.addVertex(contenedor.getId());
        }

//        System.out.println("Nodos: " + grafo.vertexSet().size());

        // Agregar aristas con pesos (distancias)
        for (Contenedor c1 : contenedores.values()) {
            PriorityQueue<Contenedor> vecinos = new PriorityQueue<>(
                    Comparator.comparingDouble(c2 -> calcularDistanciaVecinos(c1, c2))
            );

            // Añadir todos los vecinos a la cola de prioridad
            for (Contenedor c2 : contenedores.values()) {
                if (!c1.equals(c2)) {
                    vecinos.add(c2);
                }
            }

            // Conectar con los 5 vecinos más cercanos
            int conexiones = 0;
            while (!vecinos.isEmpty() && conexiones < CANTIDAD_DE_CONEXIONES) {
                Contenedor vecino = vecinos.poll();
                double distancia = calcularDistanciaVecinos(c1, vecino);

                // Agregar la arista si no existe
                if (!grafo.containsEdge(c1.getId(), vecino.getId())) {
                    grafo.setEdgeWeight(grafo.addEdge(c1.getId(), vecino.getId()), distancia);
                    conexiones++;
                }
            }
        }
        // Imprimir información del grafo
        System.out.println("Nodos: " + grafo.vertexSet().size());
        System.out.println("Aristas: " + grafo.edgeSet().size());
        this.grafo = grafo;

    }

    // Método para calcular la distancia euclidiana entre dos contenedores
    public static double calcularDistanciaVecinos(Contenedor c1, Contenedor c2) {
        double dx = c1.getLongitud() - c2.getLongitud();
        double dy = c1.getLatitud() - c2.getLatitud();
        return Math.sqrt(dx * dx + dy * dy);
    }


    public double calcularDistanciasNodo(int from, int to){
        if (!this.grafo.containsVertex(from) || !this.grafo.containsVertex(to)) {
            System.out.println("Nodos: ");
            this.grafo.vertexSet().forEach(System.out::println);
            System.out.println("FROM: " + from + " TO: " + to);
            throw new IllegalArgumentException("Uno o ambos nodos no existen en el grafo.");
        }
        DijkstraShortestPath<Integer, DefaultWeightedEdge> dijkstra = new DijkstraShortestPath<>(this.grafo);
//        System.out.println("FROM: " + from + " TO: " + to + " PESO: " + dijkstra.getPathWeight(from, to));
        double distance = dijkstra.getPathWeight(from, to);
//        if (distance == Double.POSITIVE_INFINITY) {
//            throw new IllegalArgumentException("No hay camino entre los nodos.");
//        }
        return distance;
    }

    public DefaultUndirectedWeightedGraph<Integer, DefaultWeightedEdge> getGrafo() {
        return this.grafo;
    }

    public Map<Integer, Contenedor> getContenedores() {
        return this.contenedores;
    }

    public int getCantidadNodos(){
        return this.grafo.vertexSet().size();
    }

    public static void main(String[] args) {
        // Crear contenedores
        Map<Integer, Contenedor> contenedores = LeerCSV.getContenedores(0);
//        contenedores.put(1, new Contenedor(1, -34.87333, -56.24022));
//        contenedores.put(2, new Contenedor(2, -34.87412, -56.23973));
//        contenedores.put(3, new Contenedor(3, -34.875416, -56.239506));
//        contenedores.put(4, new Contenedor(4, -34.8755, -56.23801));
//        contenedores.put(5, new Contenedor(5, -34.874973, -56.237198));
//        contenedores.put(6, new Contenedor(6, -34.874317, -56.236416));
//        contenedores.put(7, new Contenedor(7, -34.87323, -56.23591));
//        contenedores.put(8, new Contenedor(8, -34.87362, -56.237));
//        contenedores.put(9, new Contenedor(9, -34.873592, -56.237022));
//        contenedores.put(10, new Contenedor(10, -34.874096, -56.237915));
//        contenedores.put(11, new Contenedor(11, -34.873432, -56.238754));
//        contenedores.put(12, new Contenedor(12, -34.86912, -56.23935));
//        contenedores.put(13, new Contenedor(13, -34.86977, -56.238598));
//        contenedores.put(14, new Contenedor(14, -34.87083, -56.23736));
//        contenedores.put(15, new Contenedor(15, -34.871437, -56.236637));


        // Crear el grafo
        GrafoContenedores grafo = new GrafoContenedores(contenedores);
        System.out.println("Distancias: ");
        System.out.println(grafo.calcularDistanciasNodo(1,2));
        System.out.println(grafo.calcularDistanciasNodo(1,100));
    }
}


