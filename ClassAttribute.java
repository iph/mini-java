import java.util.HashMap;

class ClassAttribute extends Attribute {
	private HashMap<String, VariableAttribute> variables;
	private HashMap<String, MethodAttribute> methods;

	public ClassAttribute(int line, int col) {
		super(line, col);
        methods = new HashMap<String, MethodAttribute>();
		variables = new HashMap<String, VariableAttribute>();
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


