package minijavac;

import minijavac.ir.IR;

public abstract class CodeGenerator {
	protected IR ir;
	protected SymbolTable symbolTable;

	public CodeGenerator(IR irCode, SymbolTable symTable) {
		ir = irCode;
		symbolTable = symTable;
	}

	public abstract void generate();
}
