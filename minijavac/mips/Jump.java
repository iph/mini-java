package minijavac.mips;

public class Jump extends MIPSInstruction{
    public Jump(String l){
        label = l;
    }
    public String toString(){
        return "j " + label;
    }
}
