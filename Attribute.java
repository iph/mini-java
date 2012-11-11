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


