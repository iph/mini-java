package minijavac.mips;

public class Jump extends Instruction{
    public Jump(String l){
        label = l;
    }
    public String toString(){
        return "j " + label;
    }
}
