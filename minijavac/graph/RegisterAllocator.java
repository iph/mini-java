package minijavac.graph;
import minijavac.ir.*;
import java.util.*;
import minijavac.*;

public class RegisterAllocator{
    public static final int K = 9;
    public static final Register[] colorable = {
        Register.TEMP1, Register.TEMP2, Register.TEMP3, Register.TEMP4,
        Register.TEMP5, Register.TEMP6, Register.TEMP7,
        Register.TEMP8, Register.TEMP9, Register.TEMP10,
        Register.SAVE1, Register.SAVE2, Register.SAVE3, Register.SAVE4,
        Register.SAVE5, Register.SAVE6, Register.SAVE7, Register.SAVE8
    };
    InterferenceGraph ifg;
    Set<String> inStack;
    Stack<String> uncoloredVars;
    Set<String> precoloredVars;
    Set<String> precolorsFound;
    Live live;
    Map<String, Register> coloredVars;
    MethodIR method;
    int coloredNodesFound;

    public RegisterAllocator(MethodIR method){
        this.method = method;
        System.out.println(method);
        rewriteParams();
        live = new Live(method);
        live.computeLiveness();
        ifg = new InterferenceGraph(live);
        inStack = new HashSet<String>();
        uncoloredVars = new Stack<String>();
        precoloredVars = new HashSet<String>();
        precolorsFound = new HashSet<String>();
        coloredVars = new HashMap<String, Register>();
        precolor();
    }

    public void rewriteParams(){
        for(int i = 0; i < method.size(); i++){
            Quadruple quad = method.getQuad(i);
            if (quad.getType() == InstructionType.CALL){
                interfereParams(i-1);
                i++;
            }
        }
    }
    public void interfereParams(int place){
        Register[] params = {Register.ARG1, Register.ARG2, Register.ARG3, Register.ARG4};
        int paramsPlace = 0;

        while(place > 0 && method.getQuad(place).getType() == InstructionType.PARAM){
            place--;
        }
        place++;
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
                //TODO : Put shit onto the stack here.
            }
            place++;
        }

    }
    public void precolor(){
        for(Register reg: Register.values()){
           precoloredVars.add(reg.toString());
           coloredVars.put(reg.toString(), reg);
        }
    }
    public void color(){
        while(inStack.size() + precolorsFound.size() < ifg.vars().size()){
            int addedNode = simplify();
            if(addedNode == -1){
                System.out.println("SPILL!!!");
                return;
            }
        }

        while(uncoloredVars.size() > 0){
            select();
        }

        rewriteVariables();
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
    public ArrayList<Quadruple> RegisterAllocatedInstructions(){
        return null;
    }

}
