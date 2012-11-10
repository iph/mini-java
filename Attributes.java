public abstract class Attributes {
	private int line, column;

	public Attributes(int l, int c) {
		line = l;
		column = c;
	}

	public int getLine() {
		return line;
	}

	public int getColumn() {
		return column;
	}
}

class ClassAttributes extends Attributes {
	private ArrayList<VariableAttributes> variableList;
	private ArrayList<MethodAttributes> methodList;

	public ClassAttributes(int line, int col, ArrayList<VariableAttributes> varList, ArrayList<MethodAttributes> mList) {
		super(line, col);
		variableList = varList;
		methodList = mList;
	}

	public ArrayList<VariableAttributes> getVariableList() {
		return variableList;
	}

	public ArrayList<MethodAttributes> getMethodList() {
		return methodList;
	}
}

class MethodAttributes extends Attributes {
	private String returnType;
	private ArrayList<VariableAttributes> parameterList;

	public MethodAttributes(int line, int col, String retType, ArrayList<VariableAttributes> paramList) {
		super(line, col);
		returnType = retType;
		parameterList = paramList;
	}

	public String getReturnType() {
		return returnType;
	}

	public ArrayList<VariableAttributes> getParameterList() {
		return parameterList;
	}
}

class VariableAttributes extends Attributes {
	private String type;
	private int size; // this may only be relevant for arrays

	public VariableAttributes(int line, int col, String t) {
		super(line, col);
		type = t;
		size = 0;
	}

	public VariableAttributes(int line, int col, String t, int s) {
		super(line, col);
		type = t;
		size = s;
	}

	public String getType() {
		return type;
	}

	public int getSize() {
		return size;
	}
}
