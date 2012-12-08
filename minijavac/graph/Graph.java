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
        int size = nodes.size() + 1;
        n.name = "N" + size;
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

    public Set<Node> adjacent(Node a){
        Set<Node> temp = new HashSet<Node>();
        temp.addAll(a.successors);
        temp.addAll(a.predecessors);
        return temp;
    }

}

