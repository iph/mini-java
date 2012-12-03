package minijavac.mips;

public class Mult extends Instruction{
    public Mult(String rs, String rt){
        this.rs = rs;
        this.rt = rt;
    }

    public String toString(){
        return "mult " + this.rs + ", " + this.rt;
    }
}
