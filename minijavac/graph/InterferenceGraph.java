package minijavac.graph;
import java.util.*;

public class InterferenceGraph extends Graph{
    private Map<String, Node> vars;
    private Map<Node, String> inverseVars;

    /* Will create an empty Graph.
     *
     * This allows anyone to make their own graph.
     *  Good for testing.
     *
     */
    public InterferenceGraph(){
        super();
        vars = new HashMap<String, Node>();
        inverseVars = new HashMap<Node, String>();
    }

    /* Takes a liveness analysis and builds the interference graph.
     *
     * TODO: Figure out a way to add precolored nodes for params.
     * TODO: Add an IRMethod to the call, so we can check for calls
     *       and properly interfere
     *
     * */
    public InterferenceGraph(Live l){
        super();
        vars = new HashMap<String, Node>();
        inverseVars = new HashMap<Node, String>();
        for(Set<String> set: l.allLiveNodes()){
            for(String a: set){
                for(String b: set){
                    if(!a.equals(b)){
                        addEdge(a, b);
                   }
               }
           }
       }
    }
    /* Adds two variable strings to the interference graph. */
    public void addEdge(String a, String b){
        Node aNode, bNode;
        if(!vars.containsKey(a)){
            aNode = createNode();
            put(a, aNode);
        }else{
            aNode = vars.get(a);
        }
        if(!vars.containsKey(b)){
            bNode = createNode();
            put(b, bNode);
        }else{
            bNode = vars.get(b);
        }
        addEdge(aNode, bNode);
    }

    public void removeEdge(String a, String b){
        removeEdge(vars.get(a), vars.get(b));
        removeEdge(vars.get(b), vars.get(a));
    }

    public Set<String> adjacent(String a){
        Set<String> temp = new HashSet<String>();
        for(Node n: adjacent(vars.get(a))){
            temp.add(inverseVars.get(n));
        }

        return temp;
    }
    /* Simple utility wrapper that puts into the inverse map as well. */
    private void put(String var, Node n){
        vars.put(var, n);
        inverseVars.put(n, var);
    }

    public String toString(){
        StringBuilder str = new StringBuilder();
        for(String var: vars.keySet()){
            str.append("*-----------*\n");
            if(var.length() > 4){
                str.append("| var: " + String.format("%4s", var.substring(4)) + " |\n");
            }else{
                str.append("| var: " + String.format("%4s", var)+ " |\n");
            }

            str.append("*-----------*\n");
            for(String other: adjacent(var)){
                str.append("| " + String.format("%9s", other) + " |\n");
            }
            str.append("*-----------*\n");
        }
        return str.toString();
    }
}
