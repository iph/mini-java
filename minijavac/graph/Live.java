package minijavac.graph;
import minijavac.ir.*;
import java.util.*;
public class Live{
    Graph graph;
    MethodIR method;
    Map<Quadruple, Node> instructionMap;
    Map<Node, Quadruple> reverseInstructionMap;
    public Map<Node, Set<String>> uses, defs, liveIn, liveOut;

    public Live(MethodIR meth){
        graph = new Graph();
        method = meth;
        instructionMap = new HashMap<Quadruple, Node>();
        reverseInstructionMap = new HashMap<Node, Quadruple>();
        uses = new HashMap<Node, Set<String>>();
        defs = new HashMap<Node, Set<String>>();
        liveIn = new HashMap<Node, Set<String>>();
        liveOut = new HashMap<Node, Set<String>>();

        // Map out the graph.
        for(Quadruple quad: method){
            Node n = graph.createNode();
            instructionMap.put(quad, n);
            reverseInstructionMap.put(n, quad);
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
                    // Put a block on $a somethin;
                    use.add(quad.arg1);
                    break;
                case CALL:
                    //TODO: Put blockers on the $t0-$t9 entries.
                    def.add(quad.result);
                    def.add("$v0");
                    def.add("$a0");
                    def.add("$a1");
                    def.add("$a2");
                    def.add("$a3");
                    def.add("$t0");
                    def.add("$t1");
                    def.add("$t2");
                    def.add("$t3");
                    def.add("$t4");
                    def.add("$t5");
                    def.add("$t6");
                    def.add("$t7");
                    def.add("$t8");
                    def.add("$t9");
                    def.add("$t10");
                    break;
                case RETURN:
                    // Block the return registers.
                    use.add(quad.arg1);
                    break;
                //TODO: Milestone 9 problems.
                case ARRAY_ASSIGN:
                    def.add(quad.result);
                    if(!isInt(quad.arg2))
                        use.add(quad.arg2);
                    if(!isInt(quad.arg1))
                        use.add(quad.arg1);
                    break;
                case INDEXED_ASSIGN:
                    def.add(quad.result);
                    use.add(quad.arg1);
                    if(!isInt(quad.arg2)){
                        use.add(quad.arg2);
                    }
                    break;
                case NEW:
                    def.add(quad.result);
                    break;
                case NEW_ARRAY:
                    def.add(quad.result);
                    if(!isInt(quad.arg2)){
                        use.add(quad.arg2);
                    }
                    break;
                case LENGTH:
                    def.add(quad.result);
                    use.add(quad.arg1);
                    break;
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

    /* Do not use this function */
    private void coalesce(){
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
    public ArrayList<Node> nodes(){
        ArrayList<Node> n = new ArrayList<Node>();

        for(Node t: instructionMap.values()){
            n.add(t);
        }
        return n;
    }

    public Quadruple getInstruction(Node n){
        return reverseInstructionMap.get(n);
    }
    public Set<String> liveIn(Node n){
        return liveIn.get(n);
    }

    public Set<String> liveOut(Node n){
        return liveOut.get(n);
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
        }while(hasChanged);
    }

    public boolean union(Set<String> set1, Set<String> set2){
        return set1.addAll(set2);
    }
    public boolean difference(Set<String> set1, Set<String> set2){
        return set1.removeAll(set2);
    }
    public String toString(){
        StringBuilder str = new StringBuilder();
        for(Quadruple quad: method){
            str.append("*-----------------------*\n");
            str.append("|  ins" + String.format("%17s", quad.toString()) + " |\n");
            str.append("*-----------------------*\n");
            str.append("|      in    |   out    |\n");
            str.append("*-----------------------*\n");
            Node n = instructionMap.get(quad);
            Iterator<String> in = liveIn.get(n).iterator();
            Iterator<String> out = liveOut.get(n).iterator();
            while(in.hasNext() || out.hasNext()){
                String inStr, outStr;
                if(in.hasNext()){
                    inStr = String.format("%8s", in.next());
                }else{
                    inStr = "        ";
                }
                if(out.hasNext()){
                    outStr = String.format("%8s", out.next());
                }else{
                    outStr = "        ";
                }
                str.append("| " + inStr + "   | " + outStr + " |\n");

            }
            str.append("*-----------------------*\n");
            str.append("\n");
        }
        return str.toString();
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
