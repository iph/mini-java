package minijavac.mips;

public class Move extends MIPSInstruction{
    public Move(String rt, String rs){
        this.rt = rt;
        this.rs = rs;
    }

    public String toString(){
        return "move " + rt + ", " + rs;
    }
}
