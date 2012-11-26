package minijavac.ir;

public class Quadruple {
	public String operator;
	public String arg1;
	public String arg2;
	public String result;
	private InstructionType type;

	public Quadruple(InstructionType t) {
		operator = "";
		arg1 = "";
		arg2 = "";
		result = "";
		type = t;
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
        case PRINT:
		case CALL:
			if (result.equals("")) {
				return "call " + arg1 + ", " + arg2;
			}
			return result + " := call " + arg1 + ", " + arg2;
		case RETURN:
			return "return " + arg1;
		case ARRAY_ASSIGN:
			return result + "[" + arg1 + "] := " + arg2;
		case INDEXED_ASSIGN:
			return result + " := " + arg1 + "[" + arg2 + "]";
		case NEW:
			return result + " := new " + arg1;
		case NEW_ARRAY:
			return result + " := new " + arg1 + "[], " + arg2;
		case LENGTH:
			return result + " := length " + arg1;
		}
		return "";
	}
}
