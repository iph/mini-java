package minijavac;

public abstract class Attribute{
	private String identifier;
	private int line, column;

	public Attribute(String ident, int l, int c) {
		identifier = ident;
		line = l;
		column = c;
	}

	public String getIdentifier() {
		return identifier;
	}

	public int getLine() {
		return line;
	}

	public int getColumn() {
		return column;
	}

}


