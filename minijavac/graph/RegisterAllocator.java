package minijavac.graph;
import minijavac.ir.*;
import minijavac.mips.MIPSFrame;
import java.util.*;
import minijavac.*;

public class RegisterAllocator{
    public static final Register[] colorable = {
        Register.TEMP1, Register.TEMP2, Register.TEMP3, Register.TEMP4,
        Register.TEMP5, Register.TEMP6, Register.TEMP7,
        Register.TEMP8, Register.TEMP9, Register.TEMP10,
        Register.SAVE1, Register.SAVE2, Register.SAVE3, Register.SAVE4,
        Register.SAVE5, Register.SAVE6, Register.SAVE7, Register.SAVE8
    };
    public static final int K = colorable.length;
    InterferenceGraph ifg;
    Set<String> inStack, precoloredVars, precolorsFound, potentialMoves, potentialSpills;
    Stack<String> uncoloredVars;
    Live live;
    Map<String, Register> coloredVars;
    MethodIR method;
    MIPSFrame frame;
    Map<Quadruple, Quadruple> coalescedQuads;
    int coloredNodesFound;
    boolean canWrite;

    public RegisterAllocator(MethodIR method, MIPSFrame f){
        this.method = method;
        canWrite = true;
        frame = f;
        System.out.println("Hi");
        rewriteParams();
        live = new Live(method);
        live.computeLiveness();
        ifg = new InterferenceGraph(live);
        inStack = new HashSet<String>();
        uncoloredVars = new Stack<String>();
        coalescedQuads = new HashMap<Quadruple, Quadruple>();
        precoloredVars = new HashSet<String>();
        potentialSpills = new HashSet<String>();
        precolorsFound = new HashSet<String>();
        potentialMoves = new HashSet<String>();
        coloredVars = new HashMap<String, Register>();
        precolor();
        resolveMoves();
    }

    public void rebuild(){
        undoCoalesces();
        canWrite = true;
        live = new Live(method);
        live.computeLiveness();
        ifg = new InterferenceGraph(live);
        inStack = new HashSet<String>();
        uncoloredVars = new Stack<String>();
        coalescedQuads = new HashMap<Quadruple, Quadruple>();
        precoloredVars = new HashSet<String>();
        precolorsFound = new HashSet<String>();
        potentialMoves = new HashSet<String>();
        coloredVars = new HashMap<String, Register>();
        precolor();
        resolveMoves();

    }

    /* Will rewrite parameters to IR copy instructions. */
    public void rewriteParams(){
        int size = method.size();
        for(int i = 0; i < size; i++){
            Quadruple quad = method.getQuad(i);
            if (quad.getType() == InstructionType.CALL){
                int shiftAmount =  interfereParams(i-1, Integer.parseInt(quad.arg2));
                i += shiftAmount;
                int paramsAmount = Integer.parseInt(quad.arg2);
                if(paramsAmount > 4){
                    Quadruple q = new Quadruple(InstructionType.COPY);
                    String newTemp = method.nextTempVar();
                    q.result = newTemp;
                    q.arg1 = "" + ((paramsAmount - 4) * 4);
                    method.insertQuad(i+1, q);
                    q = new Quadruple(InstructionType.BINARY_ASSIGN);
                    q.result = "$sp";
                    q.arg1 = "$sp";
                    q.operator = "+";
                    q.arg2 = newTemp;
                    method.insertQuad(i+2, q);
                    i += 2;
                }
            }
            size = method.size();
        }
    }

    /* Figures out where to place argument */
    public int interfereParams(int place, int paramsAmount){
        Register[] params = {Register.ARG1, Register.ARG2, Register.ARG3, Register.ARG4};
        int amountShifted = 0;
        int paramsPlace = 0;
        while(place > 0 && method.getQuad(place).getType() == InstructionType.PARAM){
            place--;
        }
        place++;
        if(paramsAmount > 4){
            Quadruple q = new Quadruple(InstructionType.COPY);
            String newTemp = method.nextTempVar();
            q.result = newTemp;
            q.arg1 = "-" + ((paramsAmount - 4) * 4);
            method.insertQuad(place, q);
            q = new Quadruple(InstructionType.BINARY_ASSIGN);
            q.result = "$sp";
            q.arg1 = "$sp";
            q.arg2 = newTemp;
            q.operator = "+";
            method.insertQuad(place+1, q);
            amountShifted += 2;
            place += 1;
        }
        place ++;
        // We are all setup to start pushing params on.
        while(method.getQuad(place).getType() == InstructionType.PARAM){
            if(paramsPlace < 4){
                Quadruple q = new Quadruple(InstructionType.COPY);
                q.result = params[paramsPlace].toString();
                q.arg1 = method.getQuad(place).arg1;
                method.setQuad(place, q);
                paramsPlace++;
            }
            else{
                int storeLocation = paramsPlace - 4;
                storeLocation *= 4;
                Quadruple q = new Quadruple(InstructionType.STORE);
                q.result = "$sp";
                q.arg1 = method.getQuad(place).arg1;
                q.arg2 = "" + storeLocation;
                method.setQuad(place, q);
                paramsPlace++;
                method.setQuad(place, q);

            }
            place++;
        }
        return amountShifted;

    }

    /* Adds all starting registers to the precolored list.
     *
     * If any arg or result is names after a register, it will automatically be precolored
     * and unable to be simplified.
     */
    public void precolor(){
        for(Register reg: Register.values()){
           precoloredVars.add(reg.toString());
           coloredVars.put(reg.toString(), reg);
        }
    }

    private void resolveMoves(){
        for(String var: ifg.vars()){
           Set<String> moves = ifg.moves(var);
           for(Object o: moves.toArray()){
               String other = (String)o;
               //If the two interfere, remove the bond.
                if(ifg.hasNeighbor(var, other)){
                    ifg.removeMove(var, other);
                    ifg.removeMove(other, var);
                }
           }
        }
        for(String var: ifg.vars()){
           Set<String> moves = ifg.moves(var);
           if(moves.size() > 0){
               potentialMoves.add(var);
           }
        }


    }

    public void color(){
        while(inStack.size() + precolorsFound.size() < ifg.vars().size()){
            int addedNode = simplify();
            if(addedNode == -1){
                boolean coal = coalesce();
                if(!coal){
                    //markPotentialSpill();
                    canWrite = false;
                    System.out.println("SPILL!");
                }
            }
        }

        while(uncoloredVars.size() > 0){
            select();
        }

        rewriteVariables();
    }

    private boolean coalesce(){
        for(String potential: potentialMoves){
            for(Object pB: ifg.moves(potential).toArray()){
                String potentialB = (String) pB;
                if(precoloredVars.contains(potentialB)){
                    continue;
                }
                boolean georgeProperty = true;
                for(String otherNeighbor: ifg.adjacent(potential)){
                    if(!ifg.hasNeighbor(potentialB, otherNeighbor) && kNeighbors(otherNeighbor) >= K){
                        georgeProperty = false;
                    }
                }

                if(georgeProperty){
                    updateVar(potential, potentialB);
                    return true;
                }
                else{
                    continue;
                }
            }
        }
        return collapseNode();
    }

    private boolean collapseNode(){
        int leastSignificant = Integer.MAX_VALUE;
        String blackHole = null;
        for(String var: potentialMoves){
            int currK = kNeighbors(var);
            if(currK < leastSignificant){
                leastSignificant = currK;
                blackHole = var;
            }
        }
        if(blackHole == null){
            return false;
        }
        else{
            ifg.removeAllMoves(blackHole);
            potentialMoves.remove(blackHole);
            return true;
        }
    }
    private void updateVar(String keep, String toRemove){
        //Update all interference edges
        for(String newConnection: ifg.adjacent(toRemove)){
            ifg.addEdge(keep, newConnection);
            ifg.removeEdge(toRemove, newConnection);
        }
        for(Quadruple quad: method){
            Quadruple savedQuad = new Quadruple(quad);
            boolean wrote = false;
            if(quad.result.equals(toRemove)){
                quad.result = keep;
                wrote = true;
            }
            if(quad.arg1.equals(toRemove)){
                quad.arg1 = keep;
                wrote = true;
            }
            if(quad.arg2.equals(toRemove)){
                quad.arg2 = keep;
                wrote = true;
            }

            if(wrote && !canWrite){
                coalescedQuads.put(quad, savedQuad);
            }
        }
        ifg.removeMove(keep, toRemove);
        ifg.removeMove(toRemove, keep);
        Set<String> toRemoveMoves = ifg.moves(toRemove);
        for(String newConnection: toRemoveMoves){
            ifg.addMove(keep, newConnection);
        }
        ifg.removeAllMoves(toRemove);
        potentialMoves.remove(toRemove);
        //Update moves to properly reflect future updates.

    }

   /* simplify will attempt to find a node of less than k degree and add to the stack.
     *
     * Returns:
     *      1: If successful
     *     -1: Failed to find a node to simplify
     */
    private int simplify(){
        String bestVar = null;
        int best = -1;
        for(String var: ifg.vars()){
            if(precoloredVars.contains(var)){
                precolorsFound.add(var);
                continue;
            }
            if(potentialMoves.contains(var)){
                continue;
            }
            int currK = kNeighbors(var);

            if(currK < K && currK > best && !inStack.contains(var)){
                best = currK;
                bestVar = var;
            }
        }

        if(bestVar != null){
            inStack.add(bestVar);
            uncoloredVars.push(bestVar);
            return 1;
        }else{
            return -1;
        }
    }

    private void restoreState(){
        if(!canWrite){
            for(int i = 0; i < method.size(); i++){
                Quadruple quad = method.getQuad(i);
                if(coalescedQuads.containsKey(quad)){
                    method.replaceQuadAt(i, coalescedQuads.get(quad));
                }
            }
        }


    }
    /* Checks to see whether a variable can be colored a certain color.
     *
     * Returns true if it can be colored with that register.
     */
    private boolean neighborHasColor(String var, Register color){
        for(String other: ifg.adjacent(var)){
            if(coloredVars.containsKey(other) && coloredVars.get(other) == color){
                return true;
            }
        }
        return false;
    }

    /* Will calculate the neighbors that haven't been precolored, or simplified
     *
     * Returns the amount of neighbors that fit the above description.
     */
    private int kNeighbors(String n){
        int count = 0;
        for(String other: ifg.adjacent(n)){
            if(!inStack.contains(other)){
                count++;
            }
        }
        return count;
    }


    private void select(){
        String var = uncoloredVars.pop();
        for(int i = 0; i < colorable.length; i++){
            Register color = colorable[i];
            if(!neighborHasColor(var, color)){
                coloredVars.put(var, color);
                break;
            }
        }
    }
    private void rewriteVariables(){
       for(Quadruple quad: method){
            if(coloredVars.containsKey(quad.result)){
                quad.result = coloredVars.get(quad.result).toString();
            }
            if(coloredVars.containsKey(quad.arg1)){
                quad.arg1 = coloredVars.get(quad.arg1).toString();
            }
            if(coloredVars.containsKey(quad.arg2)){
                quad.arg2 = coloredVars.get(quad.arg2).toString();
            }
        }
        int size = method.size();
        for(int i = 0; i < size; i++){
            Quadruple quad = method.getQuad(i);
            if(!precoloredVars.contains(quad.result) && !quad.result.equals("")){
                method.remove(i);
                i--;
            }
            if(quad.getType() == InstructionType.COPY){
                if(quad.result.equals(quad.arg1)){
                   method.remove(i);
                   i--;
                }
            }
            size = method.size();
        }

    }

    private void undoCoalesces(){
        for(int i = 0; i < method.size(); i++){
            Quadruple rewritten = method.getQuad(i);
            if(coalescedQuads.containsKey(rewritten)){
                method.replaceQuadAt(i, coalescedQuads.get(rewritten));
            }
        }
    }

}
