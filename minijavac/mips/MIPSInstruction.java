package minijavac.mips;

import minijavac.Instruction;

public abstract class MIPSInstruction extends Instruction {
    String rs, rt, rd;
    String label;

    public abstract String toString();
}
