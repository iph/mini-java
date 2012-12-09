package minijavac.graph;
import java.util.*;
import minijavac.ir.*;
import minijavac.Register;

public class InterferenceGraph extends Graph{
    private Map<String, Node> vars;
    private Map<Node, String> inverseVars;
    private Map<Node, Set<String>> moves;

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
        moves = new HashMap<Node, Set<String>>();
    }

    /* Takes a liveness analysis and builds the interference graph.
     *
     *
     * */
    public InterferenceGraph(Live l){
        super();
        vars = new HashMap<String, Node>();
        inverseVars = new HashMap<Node, String>();
        moves = new HashMap<Node, Set<String>>();
        for(Node n: l.nodes()){
            addInterferenceEdges(l.liveIn(n));
            Quadruple ins = l.getInstruction(n);
            if(ins.getType() == InstructionType.COPY && !isInt(ins.arg1) && !isPreAllocated(ins.result)){
                addInterferenceMove(l.liveOut(n), ins.arg1, ins.result);
            }else{
                addInterferenceEdges(l.liveOut(n));
            }
        }
    }

    private boolean isPreAllocated(String res){
        for(Register reg: Register.values()){
            if(reg.toString().equals(res)){
                return true;
            }
        }
        return false;
    }

    /* Uses the liveness set to create interference edges.
     *
     * Takes a set of live string variables.
     * Mutates the graph by adding nodes and edges to it.
     */
    private void addInterferenceEdges(Set<String> set){
        for(String a: set){
            if(!vars.containsKey(a)){
                createNode(a);
            }
        }
        for(String a: set){
            for(String b: set){
                if(!a.equals(b)){
                    addEdge(a, b);
                }
            }
        }
    }

    public Set<String> moves(String var){
        return moves.get(vars.get(var));
    }

    public void removeAllMoves(String var){
        for(String other: moves.get(vars.get(var))){
            moves.get(vars.get(other)).remove(var);
        }
        moves.put(vars.get(var), new HashSet<String>());
    }

    public void removeMove(String var, String otherVar){
        Set<String> set = moves.get(vars.get(var));
        set.remove(otherVar);
    }

    public void addMove(String var, String otherVar){
        Set<String> moveSet = moves.get(vars.get(var));
        moveSet.add(otherVar);
        moveSet = moves.get(vars.get(otherVar));
        moveSet.add(var);

    }

    private boolean isInt(String possibleInt){
        try{
            Integer.parseInt(possibleInt);
            return true;
        }catch(Exception e){
            return false;
        }
    }

    public boolean hasNeighbor(String var, String potentialNeighbor){
        return adjacent(var).contains(potentialNeighbor);
    }
    public boolean hasMoveNodes(String var){
        return moves.get(vars.get(var)).size() > 0;
    }

    /* Same as addInterferenceEdges except deals with a move variable. */
    private void addInterferenceMove(Set<String> set, String move, String moveTo){
        for(String a: set){
            if(!vars.containsKey(a)){
                createNode(a);
            }
        }
        if(vars.containsKey(move) && vars.containsKey(moveTo)){
            Set<String> moveSet = moves.get(vars.get(moveTo));
            moveSet.add(move);
            moveSet = moves.get(vars.get(move));
            moveSet.add(moveTo);
        }

        for(String a: set){
            for(String b: set){
                if(!a.equals(b)){
                    if(!((a.equals(move) && b.equals(moveTo)) || (a.equals(moveTo) && b.equals(move)))){
                        addEdge(a, b);
                    }
                }
            }
        }

    }
    public void createNode(String var){
        Node n = super.createNode();
        moves.put(n, new HashSet<String>());
        put(var, n);
    }
    /* Adds two variable strings to the interference graph. */
    public void addEdge(String a, String b){
        Node aNode, bNode;
        aNode = vars.get(a);
        bNode = vars.get(b);
        addEdge(aNode, bNode);
    }

    public void removeEdge(String a, String b){
        removeEdge(vars.get(a), vars.get(b));
        removeEdge(vars.get(b), vars.get(a));
    }

    public Set<String> vars(){
        return vars.keySet();
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
            str.append("|~interfere~|\n");
            for(String other: adjacent(var)){
                str.append("| " + String.format("%9s", other) + " |\n");
            }
            str.append("*-----------*\n");
            if(moves.get(vars.get(var)).size() == 0){
                continue;
            }
            str.append("|  ~moves~  |\n");
            for(String other: moves.get(vars.get(var)) ){
                str.append("| " + String.format("%9s", other) + " |\n");
            }
            str.append("*-----------*\n");
        }

        return str.toString();
    }
}
