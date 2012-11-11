package ir;

public enum InstructionType {
	BINARY_ASSIGN, UNARY_ASSIGN, 
	COPY,
	JUMP, COND_JUMP,
	PARAM, CALL,
	RETURN,
	ARRAY_ASSIGN, INDEXED_ASSIGN,
	NEW, NEWARRAY,
	LENGTH
}

public class Quadruple {
	public String operator;
	public String arg1;
	public String arg2;
	public String result;
	private InstructionType type;

	private static int lastTempId = 0;

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

	public InstructionType getType() {
		return type;
	}

	public String toString() {

	}
}