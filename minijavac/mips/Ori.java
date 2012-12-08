package minijavac.mips;

public class Ori extends MIPSInstruction{
    int imm16;
    public Ori(String rs, String rt, int i){
        this.rs = rs;
        this.rt = rt;
        imm16 = i;
    }

    public String toString(){
        return "ori " + this.rs + ", " + this.rt + ", " + imm16;
    }

}
