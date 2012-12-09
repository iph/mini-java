package minijavac;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.LinkedHashMap;

public class MethodAttribute extends Attribute {
	private String returnType;
	private Map<String, VariableAttribute> parameterList;
    private HashMap<String, VariableAttribute> vars;

	public MethodAttribute(String identifier, int line, int col, String retType) {
		super(identifier, line, col);
		returnType = retType;
		parameterList = new LinkedHashMap<String, VariableAttribute>();
        vars = new HashMap<String, VariableAttribute>();
	}

	public void addParameter(String id, VariableAttribute v){
		parameterList.put(id, v);
	}

    public void addVariable(String id, VariableAttribute varAttr){
        vars.put(id, varAttr);
    }

    // Useful little function for putting a visitor into a method scope.
    public void getInMyScope(SymbolTable table){
       for(String id: vars.keySet()){
           table.put(id, vars.get(id));
       }
       //TODO Write params here as well.
       for(String id: parameterList.keySet()){
            table.put(id, parameterList.get(id));
       }
    }

	public String getReturnType() {
		return returnType;
	}

    public int parameterListSize(){
        return parameterList.keySet().size();
    }

	public VariableAttribute getParameter(int position) {
        int count = 0;
        // Holy shit is this inefficient. May have to rewrite later.
        // with a different data structure TODO.
        for(String varName: parameterList.keySet()){
            if(count == position){
                return parameterList.get(varName);
            }
            count++;
        }
        // Honestly should never get to this point.
        return null;
	}

    //Gets the name of the variable from the parameter list.
    public VariableAttribute getParameter(String id){
        return parameterList.get(id);
    }

    public boolean hasParameter(String id){
        return parameterList.containsKey(id);
    }

    public boolean hasVariable(String id){
        return vars.containsKey(id);
    }
}


