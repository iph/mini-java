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

	public ArrayList<String> getLabels(Instruction instr) {
		ArrayList<String> labels = new ArrayList<String>();
		for (Map.Entry<String, Instruction> entry : labelLoc.entrySet()) {
			if (instr == entry.getValue()) {
				labels.add(entry.getKey());
			}
		}
		return labels;
	}

	public String toString() {
		StringBuilder repr = new StringBuilder();
		// add the instructions
		for (int i = 0; i < instructions.size(); i++) {
			// add instruction labels if it has them
			ArrayList<String> labels = getLabels(instructions.get(i));
			boolean hasLabel = labels.size() > 0 ? true : false;
			for (int j = 0; j < labels.size(); j++) {
				if (j == labels.size()-1 && !labels.get(j).contains(".")) {
					repr.append(String.format("%-8s", labels.get(j) + ":"));
					hasLabel = true;
				} else {
					repr.append(String.format("%s:\n", labels.get(j)));
					hasLabel = false;
				}
			}

			if (hasLabel) {
				// add instruction
				repr.append(String.format("%s\n", instructions.get(i).toString()));
			} else {
				// add instruction
				repr.append(String.format("        %s\n", instructions.get(i).toString()));
			}
		}
		return repr.toString();
	}

	public int size() {
		return instructions.size();
	}
}