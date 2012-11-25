package minijavac;

import java.util.HashMap;

class ClassAttribute extends Attribute {
	private ClassAttribute parentClass;
	private HashMap<String, VariableAttribute> variables;
	private HashMap<String, MethodAttribute> methods;

	public ClassAttribute(String identifier, int line, int col) {
		super(identifier, line, col);
    	parentClass = null;
        methods = new HashMap<String, MethodAttribute>();
		variables = new HashMap<String, VariableAttribute>();
	}

	public void setParentClass(ClassAttribute c) {
		parentClass = c;
	}

	public void addMethod(String name, MethodAttribute m){
		methods.put(name, m);
	}

	public void addVariable(String name, VariableAttribute v){
		variables.put(name, v);
	}

	public ClassAttribute getParentClass() {
		return parentClass;
	}

	public boolean hasSuperclass(String className) {
		// do we even have a parent class?
		if (parentClass == null) {
			return false;
		}
		// check immediate parent class
		if (parentClass.getIdentifier().equals(className)) {
			return true;
		}
		// check further up the hierarchy
		return parentClass.hasSuperclass(className);
	}

	// TODO: should the get/has methods below recursively
	//       check all parent ClassAttributes?
	public MethodAttribute getMethod(String methodName){
		return methods.get(methodName);
	}

	public boolean hasMethod(String methodName){
		return methods.containsKey(methodName);
	}

	public VariableAttribute getVariable(String variableName){
		return variables.get(variableName);
	}

	public boolean hasVariable(String variableName){
        return variables.containsKey(variableName);
	}

    public void getInMyScope(SymbolTable table){
        for(String id: variables.keySet()){
            table.put(id, variables.get(id));
        }
        for(String id: methods.keySet()){
            table.put(id, methods.get(id));
        }
    }
}


