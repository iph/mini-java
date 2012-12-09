package minijavac.mips.instructions;

import minijavac.mips.*;

public class Mult extends MIPSInstruction{
    public Mult(String rs, String rt){
        this.rs = rs;
        this.rt = rt;
    }

    public String toString(){
        return "mult " + this.rs + ", " + this.rt;
    }
}
