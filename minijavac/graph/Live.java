package minijavac.graph;
import minijavac.ir.*;
import java.util.*;
public class Live{
    Graph graph;
    MethodIR method;
    Map<Quadruple, Node> instructionMap;
    Map<Node, Set<String>> uses, defs, liveIn, liveOut;

    public Live(MethodIR meth){
        graph = new Graph();
        method = meth;
        instructionMap = new HashMap<Quadruple, Node>();
        uses = new HashMap<Node, Set<String>>();
        defs = new HashMap<Node, Set<String>>();
        liveIn = new HashMap<Node, Set<String>>();
        liveOut = new HashMap<Node, Set<String>>();

        // Map out the graph.
        for(Quadruple quad: method){
            Node n = graph.createNode();
            instructionMap.put(quad, n);
            uses.put(n, new HashSet<String>());
            defs.put(n, new HashSet<String>());
            liveIn.put(n, new HashSet<String>());
            liveOut.put(n, new HashSet<String>());
        }
        // Create the edges of all the nodes
        for(Quadruple quad: method){
            Set<String> use = uses.get(instructionMap.get(quad));
            Set<String> def = defs.get(instructionMap.get(quad));
            Node n, other;

            switch(quad.getType()){
                case BINARY_ASSIGN:
                    if(!isInt(quad.arg1)){
                        use.add(quad.arg1);
                    }
                    if(!isInt(quad.arg2)){
                        use.add(quad.arg2);
                    }
                    def.add(quad.result);
                    break;
                case UNARY_ASSIGN:
                    if(!isInt(quad.arg1)){
                        use.add(quad.arg1);
                    }
                    def.add(quad.result);
                    break;
                case COPY:
                    if(!isInt(quad.arg1)){
                        use.add(quad.arg1);
                    }
                    def.add(quad.result);
                    break;
                case JUMP:
                    n = instructionMap.get(quad);
                    other = instructionMap.get(method.getQuadFromLabel(quad.arg1));
                    graph.addEdge(n, other);
                    break;
                case COND_JUMP:
                    use.add(quad.arg2);
                    n = instructionMap.get(quad);
                    other = instructionMap.get(method.getQuadFromLabel(quad.arg1));
                    graph.addEdge(n, other);
                    break;
                case PARAM:
                    //TODO: Put a block on $vX;
                    break;
                case PRINT:
                case CALL:
                    //TODO: Put blockers on the $t0-$t9 entries.
                    break;
                case RETURN:
                    // Block the return registers.
                    n = instructionMap.get(quad);
                    use.add(quad.arg1);
                    break;
                //TODO: Milestone 9 problems.
                /*
                case ARRAY_ASSIGN:
                case INDEXED_ASSIGN:
                case NEW:
                case NEW_ARRAY:
                case LENGTH:
                */
                default:
                    break;
            }
        }

        /* Add normal edges going downward. */
        for(int i = 0; i < method.size() -1; i++){
            Node to = instructionMap.get(method.getQuad(i));
            Node from = instructionMap.get(method.getQuad(i+1));
            graph.addEdge(to, from);
        }
    }
    private boolean isInt(String possibleInt){
        try{
            Integer.parseInt(possibleInt);
            return true;
        }catch(Exception e){
            return false;
        }
    }

    public void coalesce(){
        for(Quadruple quad: method){
            Node n = instructionMap.get(quad);
            if(n.outDegree() == 1){
                Node other = null;
                Set<String> toUsed, fromUsed, toDef, fromDef;
                //Bad way to get the only outdegree from it, but oh well.
                for(Node o: n.successors){
                    other = o;
                }
                //result of a branch, don't coalesce.
                if(other.inDegree() > 1){
                    continue;
                }
                System.out.println("Coalescing");
                toUsed = uses.get(n);
                toDef = defs.get(n);
                fromUsed = uses.get(other);
                fromDef = defs.get(other);

                union(fromDef, toDef);
                union(fromUsed, toUsed);

                //Rearrange the edges.
                for(Iterator<Node> i = n.predecessors.iterator(); i.hasNext(); ){
                    Node enter = i.next();
                    graph.addEdge(enter, other);
                    i.remove();
                    enter.successors.remove(n);
                }
                graph.removeEdge(n, other);
                updateCoalesce(n, other);
            }
        }
    }

    public ArrayList<Set<String>> allLiveNodes(){
        ArrayList<Set<String>> lives = new ArrayList<Set<String>>();
       for(Set<String> set: liveIn.values()){
           lives.add(set);
       }

       for(Set<String> set: liveOut.values()){
           lives.add(set);
       }

       return lives;
    }
    /* Updates any node that is equal to check to the Node to.
     *
     * Takes a node and will update the corresponding node that is passed in.
     * check: The node we are looking for.
     *
     */
    private void updateCoalesce(Node check, Node to){
        for(Map.Entry<Quadruple, Node> entry: instructionMap.entrySet()){
            if(entry.getValue() == check){
                instructionMap.put(entry.getKey(), to);
            }
        }
    }

    public void computeLiveness(){
        int i = 1;
        boolean hasChanged = false;
        do{
            hasChanged = false;
            System.out.println("Iteration " + i);
            Node last = instructionMap.get(method.getQuad(method.size()-1));
            Stack<Node> dfs = new Stack<Node>();
            dfs.push(last);
            Set<Node> seen = new HashSet<Node>();
            while(dfs.size() > 0){
                Set<String> temp, result;
                Node current = dfs.pop();
                // Populate dfs.
                for(Node pred: current.predecessors){
                    if(!seen.contains(pred)){
                        seen.add(pred);
                        dfs.push(pred);
                    }
                }
                // Updating liveIn.
                temp = (Set)((HashSet)liveOut.get(current)).clone();
                difference(temp, defs.get(current));
                result = (Set)((HashSet)uses.get(current)).clone();
                union(result, temp);
                if(result.size() == 0 && liveIn.get(current).size() == 0){
                }
                else{
                    hasChanged |= !result.equals(liveIn.get(current));
                }
                liveIn.put(current, result);

                //Updating live out.
                result = new HashSet<String>();
                for(Node s: current.successors){
                    union(result, liveIn.get(s));
                }
                if(result.size() == 0 && liveOut.get(current).size() == 0){
                }
                else{
                    hasChanged |= !result.equals(liveOut.get(current));
                }

                liveOut.put(current, result);
            }
            System.out.println(liveIn);
            System.out.println(liveOut);
            if(i == 10){
                break;
            }
            i++;
        }while(hasChanged);
    }

    public boolean union(Set<String> set1, Set<String> set2){
        return set1.addAll(set2);
    }
    public boolean difference(Set<String> set1, Set<String> set2){
        return set1.removeAll(set2);
    }

    public String toStringBlock(){
        ArrayList<Quadruple> block = new ArrayList<Quadruple>();
        Node previousNode = null;
        for(Quadruple quad: method){
            if(block.size() == 0){
                previousNode = instructionMap.get(quad);
            }
            Node currentNode = instructionMap.get(quad);
            if(currentNode != previousNode){
                System.out.print("====Block Stats {");
                System.out.println(" I am node: " + previousNode);
                System.out.println(" I connect to: " + previousNode.successors);
                System.out.println(" I am connected to: " + previousNode.predecessors);
                System.out.print("Use:" + uses.get(previousNode));
                System.out.print(", Def:" + defs.get(previousNode));
                System.out.println("}=====");

                for(Quadruple q: block){
                    System.out.println(q);
                }
                System.out.println("===========\n");
                block = new ArrayList<Quadruple>();
            }
            block.add(quad);
            previousNode = currentNode;
        }
        System.out.print("====Block Stats {");
        System.out.println(" I am node: " + previousNode);
        System.out.println(" I connect to: " + previousNode.successors);
        System.out.println(" I am connected to: " + previousNode.predecessors);
        System.out.print("Use:" + uses.get(previousNode));
        System.out.print(", Def:" + defs.get(previousNode));
        System.out.println("}=====");

        for(Quadruple q: block){
            System.out.println(q);
        }
        System.out.println("===========\n");
        return "";
    }


}
