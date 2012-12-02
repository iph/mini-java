package minijavac.mips;

public class Li extends Instruction{
	String rd;
    int imm32;

    public Li(String reg, int i){
        rd = reg;
        imm32 = i;
    }

    public String toString(){
        return "li " + rd + ", " + imm32;
    }
}
