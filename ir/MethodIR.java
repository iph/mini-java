package ir;

import java.util.*;

// TODO: should newTempVar() and newLabel() be removed from Quadruple
//       and added to this class instead so it generates 'local' things?
public class MethodIR {
	private String canonicalMethodName;
	private ArrayList<Quadruple> ir;
	private HashMap<String, Quadruple> labelLoc;
	private HashMap<String, Integer> unresolvedLabels;

	public MethodIR(String methodName) {
		canonicalMethodName = methodName;
		ir = new ArrayList<Quadruple>();
		labelLoc = new HashMap<String, Quadruple>();
		unresolvedLabels = new HashMap<String, Integer>();
	}

	public void addQuad(Quadruple quad) {
		ir.add(quad);
	}

	public void addLabel(String label, Quadruple quad) {
		labelLoc.put(label, quad);
	}

	public void addFutureLabel(String label, int index) {
		unresolvedLabels.put(label, index);
	}

	public void backpatch() {
		for (Map.Entry<String, Integer> entry : unresolvedLabels.entrySet()) {
			String label = entry.getKey();
			int index = entry.getValue();
			if (ir.size() > index) {
				addLabel(label, ir.get(index));
			} else if (ir.size() == index) {
				// a no-op quadruple must be made and associated
				// with the label. This happens when, for example,
				// the main method ends with if-else
				addQuad(new Quadruple(InstructionType.RETURN));
				addLabel(label, ir.get(index));
			} else {
				// uhhhh, something bad happened.
				System.err.println("Backpatching failed");
			}
		}
	}

	public Quadruple getQuad(int index) {
		return ir.get(index);
	}
	
	public ArrayList<String> getLabels(Quadruple quad) {
		ArrayList<String> labels = new ArrayList<String>();
		for (Map.Entry<String, Quadruple> entry : labelLoc.entrySet()) {
			if (quad == entry.getValue()) {
				labels.add(entry.getKey());
			}
		}
		return labels;
	}

	public String toString() {
		// add the method's label
		String repr = canonicalMethodName + ":\n";
		// add the method's instructions
		for (int i = 0; i < ir.size(); i++) {
			// add instruction labels if it has them
			ArrayList<String> labels = getLabels(ir.get(i));
			for (int j = 0; j < labels.size(); j++) {
				if (j == labels.size()-1) {
					repr += String.format("%s", labels.get(j) + ": ");
				} else {
					repr += String.format("%s", labels.get(j) + ":\n");
				}
			}
			// add instruction
			repr += String.format("\t%s", ir.get(i).toString() + "\n");
		}

		return repr;
	}

	public int size() {
		return ir.size();
	}
}