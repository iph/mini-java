package minijavac.mips;

public class Mflo extends Instruction{
    public Mflo(String rd){
        this.rd = rd;
    }

    public String toString(){
        return "mflo " + this.rd;
    }
}
