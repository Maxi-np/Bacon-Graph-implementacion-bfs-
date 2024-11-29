package graph;

import java.util.List;

/**
 *  Graph represents an undirected graph, where vertices store
 *  values of type Comparable.
 *  Formally, a graph G=<V,E> consists of a set of vertices V,
 *  and a relation E in VxV that describes the edges of the graph.
 */
public interface Graph<T extends Comparable<? super T>> {

    /**
     * @post Returns the number of vertices in this graph.
     */
    public int V();

    /**
     * @post Returns the number of edges in this graph.
     */
    public int E();

    /**
     * @pre !containsVertex(v).
     * @post Adds the vertex with label v to this graph.
     */
    public void addVertex(T v);

    /**
     * @post Returns true iff there is a vertex with label v
     * in this graph.
     */
    public boolean containsVertex(T v);

    /**
     * @pre v and w are vertices of the graph
     * @post Adds the undirected edge v-w to this graph.
     */
    public void addEdge(T v, T w);

    /**
     * @pre v is a vertex of the graph
     * @post Returns the list of vertices adjacent to vertex v.
     */
    public List<T> adj(T v);

}

