package minijavac.mips.instructions;

import minijavac.mips.*;

public class Nor extends MIPSInstruction{
    public Nor(String rd, String rs, String rt){
        this.rd = rd;
        this.rs = rs;
        this.rt = rt;
    }

    public String toString(){
        return "nor " + this.rd + ", " + this.rs + ", " + this.rt;
    }
}
