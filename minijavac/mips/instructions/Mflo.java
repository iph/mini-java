package minijavac.mips.instructions;

import minijavac.mips.*;

public class Mflo extends MIPSInstruction{
    public Mflo(String rd){
        this.rd = rd;
    }

    public String toString(){
        return "mflo " + this.rd;
    }
}
