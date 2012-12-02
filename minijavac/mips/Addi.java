package minijavac.mips;

public class Addi extends Instruction{
    int imm16;
    public Addi(String rs, String rt, int i){
        this.rs = rs;
        this.rt = rt;
        imm16 = i;
    }

    public String toString(){
        return "addi " + this.rs + ", " + this.rt + ", " + imm16;
    }

}
