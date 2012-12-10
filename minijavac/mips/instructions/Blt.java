package minijavac.mips.instructions;

import minijavac.mips.*;

public class Blt extends MIPSInstruction {
    public Blt(String rs, String rt, String l) {
    	this.rs = rs;
    	this.rt = rt;
        label = l;
    }

    public String toString() {
        return "blt " + rs + ", " + rt + ", " + label;
    }
}
