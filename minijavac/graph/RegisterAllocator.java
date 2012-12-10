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
    };
    public static final int K = colorable.length;
    InterferenceGraph ifg;
    Set<String> savedRegisters, inStack, precoloredVars, precolorsFound, potentialMoves, potentialSpills;
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
        rewriteParams();
        live = new Live(method);
        live.computeLiveness();
        ifg = new InterferenceGraph(live);
        inStack = new HashSet<String>();
        uncoloredVars = new Stack<String>();
        coalescedQuads = new HashMap<Quadruple, Quadruple>();
        precoloredVars = new HashSet<String>();
        savedRegisters =  new HashSet<String>();
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
                else{
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
            place ++;
        }
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
        while(true){
            while(inStack.size() + precolorsFound.size() + potentialSpills.size() < ifg.vars().size()){
                int addedNode = simplify();
                if(addedNode == -1){
                    boolean coal = coalesce();
                    if(!coal){
                        markPotentialSpill();
                        canWrite = false;
                    }
                }
            }

            while(uncoloredVars.size() > 0){
                select();
            }

            boolean fixedSpills = true;
            while(potentialSpills.size() > 0){
                fixedSpills &= selectSpill();
                if(!fixedSpills){
                    System.out.println("REAL SPILL BOYS");
                    break;
                }
            }
            if(fixedSpills){
                rewriteVariables();
                System.out.println(method);
                return;
            }
            else{
               spill();
               System.out.println(method);
               undoCoalesces();
               rebuild();
            }
        }
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
            if(potentialMoves.contains(var) || potentialSpills.contains(var)){
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

    private void spill(){
        //Identify the def instructions of node.
        String varToSpill =(String)potentialSpills.toArray()[0];
        frame.allocate(varToSpill);
        int offset = frame.get(varToSpill);
        int size = method.size();
        for(int i = 0; i < size; i++){
            Quadruple quad = method.getQuad(i);

            if(quad.result.equals(varToSpill)){
                //Make a new quad after this quad to store it.
                Quadruple storeQuad = new Quadruple(InstructionType.STORE);
                storeQuad.arg1 = method.nextTempVar();
                storeQuad.result = "$fp";
                storeQuad.arg2 = ""+offset;
                method.insertQuad(i+1, storeQuad);
                quad.result = storeQuad.arg1;
            }

            if(quad.arg1.equals(varToSpill)){
                Quadruple loadQuad = new Quadruple(InstructionType.LOAD);
                loadQuad.result = method.nextTempVar();
                loadQuad.arg1 = "$fp";
                loadQuad.arg2 = "" + offset;
                quad.arg1 = loadQuad.result;
                method.insertQuad(i-1, loadQuad);
            }

            if(quad.arg2.equals(varToSpill)){
                Quadruple loadQuad = new Quadruple(InstructionType.LOAD);
                loadQuad.result = method.nextTempVar();
                loadQuad.arg1 = "$fp";
                loadQuad.arg2 = "" + offset;
                quad.arg2 = loadQuad.result;
                method.insertQuad(i-1, loadQuad);
            }
            size = method.size();
        }
    }
    private void markPotentialSpill(){
        int mostK = 0;
        String varToSpill = null;
        for(String var: ifg.vars()){
            int kCurr = kNeighbors(var);
            if(kCurr > mostK && !precoloredVars.contains(var) && !potentialSpills.contains(var)){
                varToSpill = var;
                mostK = kCurr;
            }
        }
        System.out.println(varToSpill);
        potentialSpills.add(varToSpill);
    }
    /* Checks to see whether a variable can be colored a certain color.
     *
     * Returns true if it can be colored with that register.
     */
    private boolean neighborHasColor(String var, Register color){
        System.out.println(var);
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
            if(!inStack.contains(other) && !potentialSpills.contains(other)){
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

     private boolean selectSpill(){
        String var = (String)potentialSpills.toArray()[0];
        for(int i = 0; i < colorable.length; i++){
            Register color = colorable[i];
            if(!neighborHasColor(var, color)){
                coloredVars.put(var, color);
                potentialSpills.remove(var);
                return true;
            }
        }
        return false;
    }
    private void rewriteVariables(){
        int size = method.size();
       for(int i = 0; i < size; i++){
           Quadruple quad = method.getQuad(i);
            if(coloredVars.containsKey(quad.result)){
                saveSRegister(coloredVars.get(quad.result));
                quad.result = coloredVars.get(quad.result).toString();
            }
            if(coloredVars.containsKey(quad.arg1)){
                saveSRegister(coloredVars.get(quad.arg1));
                quad.arg1 = coloredVars.get(quad.arg1).toString();
            }
            if(coloredVars.containsKey(quad.arg2)){
                saveSRegister(coloredVars.get(quad.arg2));
                quad.arg2 = coloredVars.get(quad.arg2).toString();
            }
            size = method.size();
        }
        size = method.size();
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

    private void saveSRegister(Register save){
        Register[] sRegisters = {
            Register.SAVE1, Register.SAVE2, Register.SAVE3, Register.SAVE4,
            Register.SAVE5, Register.SAVE6, Register.SAVE7, Register.SAVE8
        };
        for(int i = 0; i < sRegisters.length; i++){
            if(sRegisters[i] == save && !savedRegisters.contains(save.toString())){
                Quadruple saveReg = new Quadruple(InstructionType.STORE);
                saveReg.arg1 = save.toString();
                frame.allocate(save.toString());
                int offset = frame.get(save.toString());
                saveReg.arg2 = "-" + offset;
                saveReg.result = "$fp";
                method.insertQuad(0, saveReg);
                Quadruple loadReg = new Quadruple(InstructionType.LOAD);
                loadReg.arg1 = "$fp";
                loadReg.arg2 = "-" + offset;
                loadReg.result = save.toString();
                method.insertQuad(method.size()-1, loadReg);
                savedRegisters.add(save.toString());
               break;
            }
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
