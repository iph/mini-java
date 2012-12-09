package minijavac.mips.instructions;

import minijavac.mips.*;

public class Ori extends MIPSInstruction{
    String imm16;

    public Ori(String rs, String rt, String i){
        this.rs = rs;
        this.rt = rt;
        imm16 = i;
    }

    public Ori(String rs, String rt, int i) {
    	this(rs, rt, ""+i);
    }

    public String toString(){
        return "ori " + this.rs + ", " + this.rt + ", " + imm16;
    }

}
