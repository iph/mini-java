package minijavac.ir;

public enum InstructionType {
	BINARY_ASSIGN, UNARY_ASSIGN,
	COPY,
	JUMP, COND_JUMP,
	PARAM, CALL, PRINT,
	RETURN,
	ARRAY_ASSIGN, INDEXED_ASSIGN,
	NEW, NEW_ARRAY,
	LENGTH
}
