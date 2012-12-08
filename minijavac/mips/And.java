package minijavac.mips;

public class And extends MIPSInstruction{
    public And(String registerToStore, String register1, String register2){
        rd = registerToStore;
        rs = register1;
        rt = register2;
    }

    public String toString(){
        return "and " + rd + ", " + rs + ", " +  rt;
    }
}
