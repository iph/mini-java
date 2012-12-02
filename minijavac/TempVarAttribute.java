package minijavac;

public class TempVarAttribute extends Attribute {
	private String type;

	public TempVarAttribute(String identifier, String t) {
		super(identifier, -1, -1);
		type = t;
	}

	public String getType() {
		return type;
	}

	public boolean isSameType(String otherType){
		return type.equalsIgnoreCase(otherType);
	}
}
