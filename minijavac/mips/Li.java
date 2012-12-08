package minijavac.mips;

public class Li extends MIPSInstruction{
    int imm32;

    public Li(String rd, int i){
        this.rd = rd;
        imm32 = i;
    }

    public String toString(){
        return "li " + rd + ", " + imm32;
    }
}
