package minijavac.mips.instructions;

import minijavac.mips.*;

public class Addi extends MIPSInstruction{
    String imm16;
    public Addi(String rs, String rt, String i) {
        this.rs = rs;
        this.rt = rt;
        imm16 = i;
    }

    public Addi(String rs, String rt, int i){
    	this(rs, rt, ""+i);
    }

    public String toString(){
        return "addi " + this.rs + ", " + this.rt + ", " + imm16;
    }

}
