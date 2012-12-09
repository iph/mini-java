package minijavac.mips.instructions;

import minijavac.mips.*;

public class Sw extends MIPSInstruction{
    private String offset;

    public Sw(String registerSavedFrom, String memLocation, String offset) {
        this.rt = registerSavedFrom;
        this.rs = memLocation;
        this.offset = offset;
    }

    public Sw(String registerSavedFrom, String memLocation, int offset) {
    	this(registerSavedFrom, memLocation, ""+offset);
    }
    
    public String toString() {
        return "sw " + this.rt + ", " + offset + "(" + this.rs + ")";
    }
}
