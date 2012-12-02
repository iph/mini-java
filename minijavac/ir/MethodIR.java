package minijavac.ir;

import java.util.*;
import minijavac.SymbolTable;

// TODO: should newLabel() be static or per-instance?
public class MethodIR {
	private String className;
	private String methodName;
	private SymbolTable symbolTable;
	private ArrayList<Quadruple> ir;
	private HashMap<String, Quadruple> labelLoc;
	private HashMap<String, Integer> unresolvedLabels;
	private int lastTempId = 0;
	private static int lastLabelId = 0;

	public MethodIR(String clsName, String methName, SymbolTable symTable) {
		className = clsName;
		methodName = methName;
		symbolTable = symTable;
		ir = new ArrayList<Quadruple>();
		labelLoc = new HashMap<String, Quadruple>();
		unresolvedLabels = new HashMap<String, Integer>();
	}

	public String canonicalMethodName() {
		return className + "." + methodName;
	}

	public String getClassName() {
		return className;
	}

	public String getMethodName() {
		return methodName;
	}

	public String nextTempVar() {
		String var = "t" + lastTempId++;
		while (symbolTable.hasId(var)) {
			var = "t" + lastTempId++;
		}
		return var;
	}

	public static String nextLabel() {
		return "L" + lastLabelId++;
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

	// FIXME: rename this method... it's not really backpatching
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

		// TODO: make a final pass through the labels to find any
		//       that point to the same quad. remove the 'duplicate'
		//       and adjust instructions that reference it
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
		String repr = canonicalMethodName() + ":\n";
		// add the method's instructions
		for (int i = 0; i < ir.size(); i++) {
			// add instruction labels if it has them
			ArrayList<String> labels = getLabels(ir.get(i));
			boolean hasLabel = labels.size() > 0 ? true : false;
			for (int j = 0; j < labels.size(); j++) {
				if (j == labels.size()-1) {
					repr += String.format("%-7s", labels.get(j) + ":");
				} else {
					repr += String.format("%s:\n", labels.get(j));
				}
			}

			if (hasLabel) {
				// add instruction
				repr += String.format("%s\n", ir.get(i).toString());
			} else {
				// add instruction
				repr += String.format("       %s\n", ir.get(i).toString());
			}
		}

		return repr;
	}

	public int size() {
		return ir.size();
	}

	public boolean hasLabel(Quadruple quad) {
		// the first quad has a special label not stored in our hash
		if (quad == getQuad(0)) {
			return true;
		}

		for (Map.Entry<String, Quadruple> entry : labelLoc.entrySet()) {
			if (quad == entry.getValue()) {
				return true;
			}
		}
		return false;
	}

	public ArrayList<ArrayList<Quadruple>> getBasicBlocks() {
		ArrayList<ArrayList<Quadruple>> basicBlocks = new ArrayList<ArrayList<Quadruple>>();

		boolean blockStarted = false;
		ArrayList<Quadruple> basicBlock;
		for (int i = 0; i < size(); i++) {
			Quadruple curQuad = getQuad(i);
			if (hasLabel(curQuad)) {
				if (blockStarted) {

				} else {

				}
				blockStarted = true;
				basicBlock = new ArrayList<Quadruple>();
			}
		}

		return basicBlocks;
	}
}