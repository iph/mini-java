public abstract class Attribute{
	private int line, column;

	public Attribute(int l, int c) {
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

class ClassAttribute extends Attribute {
	private HashMap<String, VariableAttribute> variables;
	private HashMap<String, MethodAttribute> methods;

	public ClassAttribute(int line, int col) {
		super(line, col);
		variables = new HashMap<String, VariableAttribute>();
		methodList = mList;
	}

	public void addMethod(String name, MethodAttribute m){
		methods.put(name, m);
	}

	public void addVariable(String name, VariableAttribute v){
		variables.put(name, v);
	}

	public MethodAttribute getMethod(String methodName){
		return methods.get(methodName);
	}

	public boolean hasMethod(String methodName){
		return methods.get(methodName) == null;
	}

	public VariableAttribute getVariable(String variableName){
		return variables.get(variableName);
	}

	public boolean hasVariable(String variableName){
		return variables.get(variableName) == null;
	}

}

class MethodAttribute extends Attribute {
	private String returnType;
	private ArrayList<VariableAttribute> parameterList;

	public MethodAttribute(int line, int col, String retType) {
		super(line, col);
		returnType = retType;
		parameterList = paramList;
	}

	public void addParameter(VariableAttribute v){
		parameterList.add(v);
	}

	public String getReturnType() {
		return returnType;
	}

	public VariableAttribute getParameter(int position) {
		return parameterList.get(position);
	}
}

class VariableAttribute extends Attribute {
	private String type;
	private int size; // this may only be relevant for arrays

	public VariableAttribute(int line, int col, String t) {
		super(line, col);
		type = t;
		size = 0;
	}

	public VariableAttribute(int line, int col, String t, int s) {
		super(line, col);
		type = t;
		size = s;
	}

	public String getType() {
		return type;
	}

	public boolean isSameType(String otherType){
		return type.equalsIgnoreCase(otherType);
	}
	public int getSize() {
		return size;
	}
}
