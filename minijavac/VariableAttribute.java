package minijavac;

class VariableAttribute extends Attribute {
	private String type;
	private int size; // this may only be relevant for arrays

	public VariableAttribute(String identifier, int line, int col, String t) {
		super(identifier, line, col);
		type = t;
		size = 0;
	}

	public VariableAttribute(String identifier, int line, int col, String t, int s) {
		super(identifier, line, col);
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
