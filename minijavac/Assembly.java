package minijavac;

import java.util.*;

public abstract class Assembly {
	protected ArrayList<Instruction> instructions;
	protected HashMap<String, Instruction> labelLoc;

	public Assembly() {
		instructions = new ArrayList<Instruction>();
		labelLoc = new HashMap<String, Instruction>();
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
    	return getLabel(instr) != null;
    }

	public String getLabel(Instruction instr) {
		for (Map.Entry<String, Instruction> entry : labelLoc.entrySet()) {
			if (instr == entry.getValue()) {
				return entry.getKey();
			}
		}
		return null;
	}

	public String toString() {
		StringBuilder repr = new StringBuilder();
		// add the instructions
		for (int i = 0; i < instructions.size(); i++) {
			// add instruction's label if it has one
			if (hasLabel(instructions.get(i))) {
				String label = getLabel(instructions.get(i));
				if (label.contains(".")) {
					repr.append(String.format("%s:\n", label));
				} else {
					repr.append(String.format("%-7s", label + ":"));
				}
			}
			// add instruction
			repr.append(String.format("       %s\n", instructions.get(i).toString()));
		}
		return repr.toString();
	}

	public int size() {
		return instructions.size();
	}
}