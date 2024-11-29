package bacon;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import graph.GrafoBacon;

/**
 * La clase SixDegrees implenenta diferentes funcionalidades para resolver el
 * juego de los seis grados de Bacon.
 */

public class SixDegrees {

    private GrafoBacon<String> grafo; // nuestro grafo;
    private Map<String, Boolean> tipoNodo; //  para discriminar de pelicula o actor.

    /**
     * @post Construye una instancia de SixDegrees con la información contenida en
     *       la base de datos fileName.
     * @param fileName  el nombre de un archivo que contine nombres de películas y
     *                  sus respectivos actores y actrices.
     * @param delimiter el caracter especial que permite identificar la separación
     *                  entre los elementos del archivo fileName.
     */
    public SixDegrees(String fileName, String delimiter) {
        grafo = new GrafoBacon<>();
        tipoNodo = new HashMap<>(); 
        cargarDatos(fileName, delimiter);

    }

    private void cargarDatos(String fileName, String delimiter) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                // Separar la línea en película y actores
                String[] partes = linea.split(delimiter);
                if (partes.length > 1) {
                    String pelicula = partes[0]; // La primera parte es la película
                    List<String> actores = Arrays.asList(Arrays.copyOfRange(partes, 1, partes.length)); // Resto son
                                                                                                        // actores
                    agregarPeliculaYActores(pelicula, actores);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }
    }

    private void agregarPeliculaYActores(String pelicula, List<String> actores) {
        // Agregar la película como un nodo al grafo y al mapa
        if (!grafo.containsVertex(pelicula)) {
            grafo.addVertex(pelicula);
            tipoNodo.put(pelicula, true); // true significa que es una película
        }

        // Agregar cada actor al grafo y conectar con la película
        for (String actor : actores) {
            actor = actor.trim(); // Eliminar posibles espacios en blanco
            if (!grafo.containsVertex(actor)) {
                grafo.addVertex(actor);
                tipoNodo.put(actor, false); // false significa que es un actor
            }
            // Conectar la película con el actor
            if (!grafo.adj(pelicula).contains(actor)) {
                grafo.addEdge(pelicula, actor); // Película -> Actor
                grafo.addEdge(actor, pelicula); // Actor -> Película
            }
        }
    }

    /**
     * @pre act1 y act2 deben existir en el archivo pasado como parametro al
     *      constructor.
     * @post Retorna la película en la que trabajaron juntos act1 y act2, o null si
     *       no fueron co-protagonistas.
     */
    public String coStars(String act1, String act2) {

        if (act1.equals(act2))
            throw new IllegalArgumentException();
        // si no existen, no sirve buscar.
        if (!(grafo.containsVertex(act1) || grafo.containsVertex(act2)))
            return null;

        // en la lista de peliculas de act1, debemos buscar si algun elemento de esa
        // lista
        // esta contenido o se enceuntra tambien en la lista de peliculas de act2
        for (String pelicula : grafo.adj(act1)) {

            if (grafo.adj(act2).contains(pelicula))// si la lista de peliculas de act2 contiene "pelicula"..
                return pelicula;// signfica que actuan en la misma pelicula.

        }

        return null;// los actores no tienen pelicula en comun;
    }

    /**
     * @pre act1 y act2 deben existir en el archivo pasado como parametro al
     *      constructor.
     * @post Retorna la distancia de act1 a act2, donde distancia esta dada por:
     *       1, si trabajaron en la misma película
     *       (i+1), si act2 esta relacionado/a con algun actor o actriz que tiene
     *       distancia i con act1.
     */
    public int degreesOfSeparation(String act1, String act2) {

        // si no existen en el grafo, no sirve buscar.
        if (!(grafo.containsVertex(act1) || grafo.containsVertex(act2)))
             throw new IllegalArgumentException();
        if(act1.equals(act2)) return 0;
        // si directamente trabajaron en  la misma pelicula retornamos 1;
        if (coStars(act1, act2) != null)
            return 1;

        // usamos BFS con una queue y un map para tener el historial de nodos previos.
        Queue<String> queue = new LinkedList<>();
        Map<String, String> prev = new HashMap<>(); // Para rastrear el camino(peliculas) desde act1 hasta act2.

        queue.add(act1);
        prev.put(act1, null); 

        while (!queue.isEmpty()) {

            String current = queue.poll();

            for (String vecino : grafo.adj(current)) {// recorro la adj de current en busca de act2
                // como esta diseñado el grafo(bipartito), si quiero encontrar a act2 --> current  debe ser una peli.
            
                if (vecino.equals(act2)) {
                    //llegado este punto current es la utltima pelicula antes de act2.
                    String temp = current;
                    int distTotal = 1;// cuenta tanto actores como peliculas.
                    while (prev.get(temp) != null) {// ciclo hasta llegar a act1(que tiene como previo a null)

                        temp = prev.get(temp);
                        distTotal++;

                    }
                    
                    distTotal = distTotal - (distTotal / 2); // descontar los actores.
                    return distTotal;

                }

                // Si aún no hemos visitado este actor o película, lo agregamos a la cola
                if (!prev.containsKey(vecino)) {
                    queue.add(vecino);
                    prev.put(vecino, current); // Registramos el nodo predecesor/previo (actor/película anterior)
                }
            }
        }

        // Si no encontramos una conexión entre act1 y act2
        return -1;
    }

    /**
     * @pre act1 y act2 deben existir en el archivo pasado como parametro al
     *      constructor.
     * @post Retorna la lista de actores y actrices involucrados en el camino para
     *       obtener la menor distancia de act1 a act2.
     */
    public List<String> pathOfSeparation(String act1, String act2) { 
        if(!(grafo.containsVertex(act1)) || !(grafo.containsVertex(act2))){ //Verifico la precondicion
            throw new IllegalArgumentException ("No se encuentra el o los actores");
        }
        if(coStars(act1, act2) != null){ //Si tienen una pelicula en comun
            return Arrays.asList(act1, coStars(act1, act2), act2);
        }
        Queue<String> queue = new LinkedList<>();
        Map<String, String> prev = new HashMap<>(); // Registro de los predecesores
        Set<String> visited = new HashSet<>();
        queue.add(act1);
        visited.add(act1);
        prev.put(act1, null);
        while (!queue.isEmpty()) { //BFS para encontrar el camino más corto entre los actores
            String current = queue.poll();
            for (String neighbor : grafo.adj(current)) { //Recorro todos los vecinos
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    prev.put(neighbor, current);
                    queue.add(neighbor);
                    if (neighbor.equals(act2)) { //Si encontramos act2, reconstruimos el camino
                        return reconstruirCamino(prev, act1, act2);
                    }
                }
            }
        }
        return new LinkedList<>(); //Si no hay camino entre act1 y act2, devolvemos una lista vacía
    }

    private List<String> reconstruirCamino(Map<String, String> prev, String start, String end) {
        LinkedList<String> path = new LinkedList<>();
        for (String at = end; at != null; at = prev.get(at)) {
            if(!esPelicula(at)){ //Diferenciamos de pelicula y de actor para construir el camino correctamente
            path.addFirst(at);
            }
        }
        return path;
    }

    /**
     * @pre movie debe existir en el archivo pasado como parametro al constructor.
     * @post Retorna los actores y actrices que protagonizan movie.
     */

    public Set<String> actors(String movie) {
        //Verifica si el nodo es una pelicula
        if(!esPelicula(movie)){
            throw new IllegalArgumentException(); //Si no es una pelicula lanza una excepcion
        }
        //Actores es un conjunto vacio que almacena los nombres de los actores asociados a la movie
        Set<String> actores = new HashSet<>();
        for(String neighbor : grafo.adj(movie)){        //Recorre todos los nodos vecinos del nodo
            if(!esPelicula(neighbor)){                  //Si el vecino no es una pelicula es pq es una actor
                actores.add(neighbor);                  //Agrega el actor al conjunto
            }
        }
        //Por ultimo devuelve el conjunto de actores que participaron en la pelicula
        return actores;
    }

    /**
     * @pre act debe existir en el archivo pasado como parametro al constructor.
     * @post Retorna todas las peliculas en las que actua act.
     */
    public Set<String> movies(String act) {
        //verifica si el actor esta dentro del grafo.
        if(!grafo.containsVertex(act))
            throw new IllegalArgumentException();
        //Verifica si el nodo dado es un actor
        if(esPelicula(act)){
            throw new IllegalArgumentException();
        }
        //peliculas es un conjunto vacio que almacena los nombres de las peliculas asociadas al actor
        Set<String> peliculas = new HashSet<>();
        for(String neighbor : grafo.adj(act)){              //Recorre todos los nodos vecinos del nodo
            if(esPelicula(neighbor)){                       //Si el vecino es una pelicula significa que el actor participo en esa pelicula 
                peliculas.add(neighbor);                    //Agrego la pelicula al conjunto
            }
        }
        //Por ultimo devuelve el conjunto de peliculas en las que participo el actor
        return peliculas;
    }

    public boolean esPelicula(String cadena){
        
        return tipoNodo.getOrDefault(cadena, false); // Devuelve false si el nodo no está en el mapa
        
    }
}
