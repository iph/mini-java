package minijavac.mips.instructions;

import minijavac.mips.*;

public class Lw extends MIPSInstruction{
    private String offset;

    public Lw(String register, String memLocation, String offset) {
        this.rt = register;
        this.rs = memLocation;
        this.offset = offset;
    }

    public Lw(String register, String memLocation, int offset) {
    	this(register, memLocation, ""+offset);
    }
    
    public String toString() {
        return "lw " + this.rt + ", " + offset + "(" + this.rs + ")";
    }
}
