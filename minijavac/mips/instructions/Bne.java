package minijavac.mips.instructions;

import minijavac.mips.*;

public class Bne extends MIPSInstruction {
    public Bne(String rs, String rt, String l) {
    	this.rs = rs;
    	this.rt = rt;
        label = l;
    }

    public String toString() {
        return "bne " + rs + ", " + rt + ", " + label;
    }
}
