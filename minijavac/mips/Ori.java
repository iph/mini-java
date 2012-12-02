package minijavac.mips;

public class Ori extends Instruction{
    int imm;
    public Ori(String rs, String rt, int i){
        this.rs = rs;
        this.rt = rt;
        imm = i;
    }

    public String toString(){
        return "ori " + this.rs + ", " + this.rt + ", " + imm;
    }

}
