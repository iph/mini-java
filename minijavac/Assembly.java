package minijavac;

import java.util.*;

public abstract class Assembly {
	protected ArrayList<Instruction> instructions;
	protected HashMap<String, Instruction> labelLoc;

	public Assembly() {
		instructions = new ArrayList<Instruction>();
	}

	public void addInstructions(ArrayList<Instruction> instructions) {
		for (Instruction instr : instructions) {
			addInstruction(instr);
		}
	}

	public void addInstruction(Instruction instruction) {
		instructions.add(instruction);
	}

	public Instruction getInstruction(int index) {
		return instructions.get(index);
	}

	public void addLabel(String label, Instruction instr) {
		labelLoc.put(label, instr);
	}

    public Instruction getInstructionFromLabel(String label) {
        return labelLoc.get(label);
    }

	public boolean hasLabel(Instruction instr) {
		for (Map.Entry<String, Instruction> entry : labelLoc.entrySet()) {
			if (instr == entry.getValue()) {
				return true;
			}
		}
		return false;
	}

	public String toString() {
		StringBuffer listing = new StringBuffer();
		for (Instruction instruction : instructions) {
			listing.append(instruction.toString() + "\n");
		}
		return listing.toString();
	}
}