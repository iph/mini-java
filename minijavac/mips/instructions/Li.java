package minijavac.mips.instructions;

import minijavac.mips.*;

public class Li extends MIPSInstruction{
    String imm32;

    public Li(String rd, String i) {
    	this.rd = rd;
    	imm32 = i;
    }
    
    public Li(String rd, int i){
        this(rd, ""+i);
    }

    public String toString(){
        return "li " + rd + ", " + imm32;
    }
}
