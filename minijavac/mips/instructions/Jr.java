package minijavac.mips.instructions;

import minijavac.mips.*;

public class Jr extends MIPSInstruction{
    public Jr(String register) {
        rs = register;
    }

    public String toString() {
        return "jr " + rs;
    }
}
