package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


// cada nodo es una pelicula o un actor. (String)
public class GrafoBacon<T extends Comparable<? super T>> implements Graph<T> {

    // el numero de vertices del grafo.
    private final int V;
    // numero de arcos del grafo.
    private int E;
    // nuestra lista de adyacencia :
    // creo conviene map porque asi tratamos a actores y pelis como nodos y en la
    // lista de adyancentes de cada uno solo agregamos pelis en el caso de los
    // actores y viceversa
    // para las peliculas.
    private HashMap<T, List<T>> adjList;

    public GrafoBacon() {
        
        this.V = 0;
        this.E = 0;
        adjList = new HashMap<>();

    }

    /**
     * @post Returns the number of vertices in this graph.
     */
    public int V() {
        return V;
    }

    /**
     * @post Returns the number of edges in this graph.
     */
    public int E() {
        return E;
    }

    /**
     * @pre !containsVertex(v).
     * @post Adds the vertex with label v to this graph.
     */
    public void addVertex(T v){
        if(!adjList.containsKey(v))
            adjList.put(v, new ArrayList<>());
        

    }

    /**
     * @post Returns true iff there is a vertex with label v
     *       in this graph.
     */
    public boolean containsVertex(T v) {

        return adjList.containsKey(v);

    }

    /**
     * 
     * @pre v and w are vertices of the graph
     * @post Adds the undirected edge v-w to this graph.
     */
    public void addEdge(T v, T w){
        //aca controlamos que no ya no esten en la adjList como vertices;
        if(!adjList.containsKey(v))
            throw new IllegalArgumentException();
        if(!adjList.containsKey(w))
            throw new IllegalArgumentException();
        
        //hay que agregarlos de modo tal, que tanto la pelicula como el actor queden conectados
        //controlamos que ya no esten conectados de antes. 
        if(!adjList.get(v).contains(w))
            adjList.get(v).add(w);
        
        if(!adjList.get(w).contains(v))
            adjList.get(w).add(v);
        
        
        
    }

    /**
     * @pre v is a vertex of the graph
     * @post Returns the list of vertices adjacent to vertex v.
     */
    public List<T> adj(T v){

        if(!adjList.containsKey(v))
            return null;
        
        return adjList.get(v);
    }
}
