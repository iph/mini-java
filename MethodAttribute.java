import java.util.ArrayList;
import java.util.HashMap;

class MethodAttribute extends Attribute {
	private String returnType;
	private ArrayList<VariableAttribute> parameterList;
    private ArrayList<String> parameterIDList;
    private HashMap<String, VariableAttribute> vars;

	public MethodAttribute(int line, int col, String retType) {
		super(line, col);
		returnType = retType;
        parameterIDList = new ArrayList<String>();
		parameterList = new ArrayList<VariableAttribute>();
        vars = new HashMap<String, VariableAttribute>();
	}

	public void addParameter(VariableAttribute v){
		parameterList.add(v);
	}

    public void addVariable(String id, VariableAttribute varAttr){
        vars.put(id, varAttr);
    }

    public void getInMyScope(SymbolTable table){
       for(String id: vars.keySet()){
           table.put(id, vars.get(id));
       }
       //TODO Write params here as well.
    }

	public String getReturnType() {
		return returnType;
	}

	public VariableAttribute getParameter(int position) {
		return parameterList.get(position);
	}
}


