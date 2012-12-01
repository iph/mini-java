package minijavac.graph;
import java.util.*;

/*
 * Graph class:
 *      N is the vertex or "N"ode type.
 *      E is what the edges will hold. If nothing,
 *        just set them to Integer or object and don't use them..
 */
public class Graph{
    Set<Node> nodes;
    int vertices;
    int edges;

    Graph(){
        nodes = new HashSet<Node>();
    }

    public Node createNode(){
        Node n = new Node(this);
        nodes.add(n);
        return n;
    }

    /*
     * If you try to add an edge to a vertex you haven't inserted yet,
     * this function will return false, otherwise true.
     */
    public void addEdge(Node from, Node to){
        from.successors.add(to);
        to.predecessors.add(from);
    }

    public void removeEdge(Node from, Node to){
        from.successors.remove(to);
        to.predecessors.remove(from);
    }

}

class Node{
    Set<Node> predecessors;
    Set<Node> successors;

    public Node(Graph g){
        predecessors = new HashSet<Node>();
        successors = new HashSet<Node>();
    }

    public int inDegree(){
        return predecessors.size();
    }

    public int outDegree(){
        return successors.size();
    }

    public int degree(){
        return inDegree() + outDegree();
    }

    public boolean goesTo(Node n){
        return this.successors.contains(n);
    }

    public boolean comesFrom(Node n){
        return this.predecessors.contains(n);
    }

    public boolean adjacent(Node n){
        return goesTo(n) || comesFrom(n);
    }
}


