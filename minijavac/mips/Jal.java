package minijavac.mips;

public class Jal extends Instruction{
    public Jal(String l){
        label = l;
    }
    public String toString(){
        return "jal " + label;
    }
}
