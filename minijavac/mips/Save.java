package minijavac.mips;

public class Save extends Instruction{
    private int offset;
    public  Save(String registerSavedFrom, int offset, String memLocation){
        this.rs = registerSavedFrom;
        this.rt = memLocation;
        this.offset = offset;
    }
    public String toString(){
        return "sw " + this.rs + ", " + offset + "(" + this.rt + ")";
    }
}
