package minijavac.mips.instructions;

import minijavac.mips.*;

public class Jal extends MIPSInstruction{
    public Jal(String l){
        label = l;
    }
    public String toString(){
        return "jal " + label;
    }
}
