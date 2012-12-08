package minijavac.graph;
import java.util.*;

public class Node{
    public String name;
    public Set<Node> predecessors;
    public Set<Node> successors;

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

    public String toString(){
        return name;
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

    public Set<Node> adjacentNodes(){
        Set<Node> all = new HashSet<Node>();
        all.addAll(predecessors);
        all.addAll(successors);
        return all;
    }
}


