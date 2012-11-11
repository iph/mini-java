import java.util.ArrayList;
class MethodAttribute extends Attribute {
	private String returnType;
	private ArrayList<VariableAttribute> parameterList;

	public MethodAttribute(int line, int col, String retType) {
		super(line, col);
		returnType = retType;
		parameterList = new ArrayList<VariableAttribute>();
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


