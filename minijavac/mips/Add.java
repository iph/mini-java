package minijavac.mips;

public class Add extends Instruction{
    public Add(String rd, String rs, String rt){
        this.rd = rd;
        this.rs = rs;
        this.rt = rt;
    }

    public String toString(){
        return "add " + this.rd + ", " + this.rs + ", " + this.rt;
    }
}
