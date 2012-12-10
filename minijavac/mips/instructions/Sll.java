package minijavac.mips.instructions;

import minijavac.mips.*;

public class Sll extends MIPSInstruction{
    String imm16;
    public Sll(String rs, String rt, String i) {
        this.rs = rs;
        this.rt = rt;
        imm16 = i;
    }

    public Sll(String rs, String rt, int i){
    	this(rs, rt, ""+i);
    }

    public String toString(){
        return "sll " + this.rs + ", " + this.rt + ", " + imm16;
    }

}
