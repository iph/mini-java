package minijavac.ir;

import java.util.*;
import minijavac.SymbolTable;

// TODO: should newLabel() be static or per-instance?
public class MethodIR implements Iterable<Quadruple>{
	private String canonicalMethodName;
	private SymbolTable symbolTable;
	private ArrayList<Quadruple> ir;
	private HashMap<String, Quadruple> labelLoc;
	private HashMap<String, Integer> unresolvedLabels;
	private int lastTempId = 0;
	private static int lastLabelId = 0;

	public MethodIR(String methodName, SymbolTable symTable) {
		canonicalMethodName = methodName;
		symbolTable = symTable;
		ir = new ArrayList<Quadruple>();
		labelLoc = new HashMap<String, Quadruple>();
		unresolvedLabels = new HashMap<String, Integer>();
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

    public Iterator<Quadruple> iterator(){
        return ir.iterator();
    }

	public void addLabel(String label, Quadruple quad) {
		labelLoc.put(label, quad);
	}

	public void addFutureLabel(String label, int index) {
		unresolvedLabels.put(label, index);
	}

    public Quadruple getQuadFromLabel(String label){
        return labelLoc.get(label);
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

    /* Will put a quad right before another one in the method.
     *
     */
    public void insertQuad(int index, Quadruple ins){
        ir.add(index, ins);
    }
    public void setQuad(int index, Quadruple other){
        Quadruple temp = ir.get(index);
        temp.result = other.result;
        temp.arg1 = other.arg1;
        temp.arg2 = other.arg2;
        temp.setType(other.getType());
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
}
