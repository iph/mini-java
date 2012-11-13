package ir;

public class Quadruple {
	public String operator;
	public String arg1;
	public String arg2;
	public String result;
	private InstructionType type;

	private static int lastTempId = 0;
	private static int lastLabelId = 0;

	public Quadruple(InstructionType t) {
		operator = "";
		arg1 = "";
		arg2 = "";
		result = "";
		type = t;
	}

	public static String nextTempVar() {
		return "t" + lastTempId++;
	}

	public static String nextLabel() {
		return "l" + lastLabelId++;
	}

	public InstructionType getType() {
		return type;
	}

	public String toString() {
		switch (type) {
		case BINARY_ASSIGN:
			return result + " := " + arg1 + " " + operator + " " + arg2;
		case UNARY_ASSIGN:
			return result + " := " + operator + " " + arg1;
		case COPY:
			return result + " := " + arg1;
		case JUMP:
			return "goto " + arg1;
		case COND_JUMP:
			return "iftrue " + arg2 + " goto " + arg1;
		case PARAM:
			return "param " + arg1;
		case CALL:
			return "call " + arg1 + ", " + arg2;
		case RETURN:
			return "return " + arg1;
		case ARRAY_ASSIGN:
			return result + "[" + arg1 + "] := " + arg2;
		case INDEXED_ASSIGN:
			return result + " := " + arg1 + "[" + arg2 + "]";
		case NEW:
			return "new " + arg1;
		case NEW_ARRAY:
			return "new " + arg1 + "[]";
		case LENGTH:
			return "length " + arg1;
		}
		return "";
	}
}