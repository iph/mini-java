package minijavac.mips;

import minijavac.Instruction;

public abstract class MIPSInstruction extends Instruction {
    protected String rs, rt, rd;
    protected String label;

    public abstract String toString();
}
