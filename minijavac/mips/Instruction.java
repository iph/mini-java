package minijavac.mips;

public abstract class Instruction{
    String rs, rt, rd;
    String label;

    public abstract String toString();
}
