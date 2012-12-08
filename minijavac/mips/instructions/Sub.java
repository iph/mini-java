package minijavac.mips.instructions;

import minijavac.mips.*;

public class Sub extends MIPSInstruction{
    public Sub(String rd, String rs, String rt){
        this.rd = rd;
        this.rs = rs;
        this.rt = rt;
    }

    public String toString(){
        return "sub " + this.rd + ", " + this.rs + ", " + this.rt;
    }
}
