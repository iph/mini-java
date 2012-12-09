package minijavac.ir;

import java.util.*;
import minijavac.SymbolTable;

public class MethodIR implements Iterable<Quadruple>{
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

	public void replaceQuadAt(int index, Quadruple quad) {
		ArrayList<Quadruple> quads = new ArrayList<Quadruple>();
		quads.add(quad);
		replaceQuadAt(index, quads);
	}

	public void replaceQuadAt(int index, ArrayList<Quadruple> quads) {
		// if the quad we're replacing had a label, update the label to point
		// to the first quad in our list
		if (index > 0 && hasLabel(getQuad(index))) {
			String label = getLabel(getQuad(index));
			addLabel(label, quads.get(0));
		}

		// replace the quad at index and insert the others after it
		ir.set(index, quads.get(0));
		for (int i = 1; i < quads.size(); i++) {
			ir.add(index + i, quads.get(i));
		}
	}

    public Iterator<Quadruple> iterator() {
        return ir.iterator();
    }

	public void addLabel(String label, Quadruple quad) {
		labelLoc.put(label, quad);
	}

	public void addFutureLabel(String label, int index) {
		unresolvedLabels.put(label, index);
	}

    public Quadruple getQuadFromLabel(String label){
    	// the first quad has a special label not stored in our hash
    	if (canonicalMethodName().equals(label)) {
    		return getQuad(0);
    	}

        return labelLoc.get(label);
    }

	public boolean hasLabel(Quadruple quad) {
		return getLabel(quad) != null;
	}

    public void remove(int index){
        if(hasLabel(getQuad(index))){
            for(String label: getLabels(getQuad(index))){
                addLabel(label, getQuad(index+1));
            }
        }
        ir.remove(index);
    }
	public String getLabel(Quadruple quad) {
		for (Map.Entry<String, Quadruple> entry : labelLoc.entrySet()) {
			if (quad == entry.getValue()) {
				return entry.getKey();
			}
		}
		return null;
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

		// make a final pass through the labels to find any
		// that point to the same quad. remove the 'duplicate'
		// and adjust instructions that reference it
		for (Quadruple quad : this) {
			ArrayList<String> labels = getLabels(quad);
			if (labels.size() > 1) {
				// we have more than one label for this quad, so get rid
				// of all but the first
				String label = labels.get(0);
				for (int i = 1; i < labels.size(); i++) {
					String unwantedLabel = labels.get(i);
					labelLoc.remove(unwantedLabel);
					// update all references to the 'new' label
					for (int j = 0; j < ir.size(); j++) {
						switch (ir.get(j).getType()) {
						case JUMP:
						case COND_JUMP:
							if (ir.get(j).arg1.equals(unwantedLabel)) {
								ir.get(j).arg1 = label;
							}
							break;
						}
					}
				}
			}
		}
	}

	public Quadruple getQuad(int index) {
		return ir.get(index);
	}

    /* Will put a quad right before another one in the method.
     *
     */
    public void insertQuad(int index, Quadruple ins) {
        ArrayList<Quadruple> newQuads = new ArrayList<Quadruple>();
        Quadruple oldQuad = getQuad(index);
        newQuads.add(ins);
        newQuads.add(oldQuad);
        replaceQuadAt(index, newQuads);
    }

    public void setQuad(int index, Quadruple other) {
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
		StringBuilder repr = new StringBuilder();
		repr.append(canonicalMethodName() + ":\n");
		// add the method's instructions
		for (int i = 0; i < ir.size(); i++) {
			// add instruction labels if it has them
			ArrayList<String> labels = getLabels(ir.get(i));
			boolean hasLabel = labels.size() > 0 ? true : false;
			for (int j = 0; j < labels.size(); j++) {
				if (j == labels.size()-1) {
					repr.append(String.format("%-7s", labels.get(j) + ":"));
				} else {
					repr.append(String.format("%s:\n", labels.get(j)));
				}
			}

			if (hasLabel) {
				// add instruction
				repr.append(String.format("%s\n", ir.get(i).toString()));
			} else {
				// add instruction
				repr.append(String.format("       %s\n", ir.get(i).toString()));
			}
		}

		return repr.toString();
	}

	public int size() {
		return ir.size();
	}
}
