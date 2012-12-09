package minijavac;

import java.util.HashMap;

public class ClassAttribute extends Attribute {
	private ClassAttribute parentClass;
    private String parentClassName;
	private HashMap<String, VariableAttribute> variables;
	private HashMap<String, MethodAttribute> methods;
	int size;

	public ClassAttribute(String identifier, int line, int col) {
		super(identifier, line, col);
    	parentClass = null;
        methods = new HashMap<String, MethodAttribute>();
		variables = new HashMap<String, VariableAttribute>();
		size = -1;
	}

    public void setParentClassName(String s){
        parentClassName = s;
    }

	public void setParentClass(ClassAttribute c) {
		parentClass = c;

	}

    public boolean isSameType(String type){
        if(type.equals(this.getIdentifier())){
            return true;
        }
        else if(parentClass != null){
            return parentClass.isSameType(type);
        }
        else{
            return false;
        }
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

    public String getParentClassName(){
        return parentClassName;
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
        if(methods.containsKey(methodName)){
            return methods.get(methodName);
        }
        else if(parentClass != null){
            return parentClass.getMethod(methodName);
        }
        else{
            return null;
        }
	}

	public boolean hasMethod(String methodName){
        if(methods.containsKey(methodName)){
            return true;
        }
        else if(parentClass != null){
            return parentClass.hasMethod(methodName);
        }
        else{
            return false;
        }
	}

	public VariableAttribute getVariable(String variableName){
		return variables.get(variableName);
	}

	public HashMap<String, VariableAttribute> getVariables() {
		return variables;
	}

	public boolean hasVariable(String variableName){
        return variables.containsKey(variableName);
	}

	public boolean inheritsMethod(String methodName) {
		// we have the method, but it's not in our method list
		return (hasMethod(methodName) && !(methods.containsKey(methodName)));
	}

	public ClassAttribute getClassDefiningMethod(String methodName) {
		if (!inheritsMethod(methodName)) {
			return this;
		}

		return parentClass.getClassDefiningMethod(methodName);
	}

	private void getInMyInheritanceScope(SymbolTable table) {
        for(String id: methods.keySet()){
            table.put(id, methods.get(id));
        }
        if(parentClass != null){
            parentClass.getInMyInheritanceScope(table);
        }
	}

    public void getInMyScope(SymbolTable table){
        for(String id: variables.keySet()){
            table.put(id, variables.get(id));
        }
        for(String id: methods.keySet()){
            table.put(id, methods.get(id));
        }
        if(parentClass != null){
            parentClass.getInMyInheritanceScope(table);
        }
    }

    public void setSize(int bytes) {
    	size = bytes;
    }

    public int getSize() {
    	return size;
    }
}


