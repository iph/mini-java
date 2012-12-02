package minijavac.mips;

import java.util.*;
import java.io.*;
import minijavac.CodeGenerator;
import minijavac.ir.IR;
import minijavac.SymbolTable;

public class MIPSCodeGenerator extends CodeGenerator {
	private ArrayList<Instruction> instructions;
	private MIPSTranslator translator;

	public MIPSCodeGenerator(IR ir, SymbolTable symbolTable) {
		super(ir, symbolTable);
		instructions = new ArrayList<Instruction>();
		translator = new MIPSTranslator(symbolTable);
	}

	public void generate() {
		instructions = translator.translate(ir);
		for (Instruction ins : instructions) {
			System.out.println(ins);
		}

		System.out.print("\n" + getRuntimeLibrary());
	}

	private String getRuntimeLibrary() {
		StringBuffer library = new StringBuffer();
		try {
			FileInputStream in = new FileInputStream("minijavac/mips/library.asm");
			Scanner scanner = new Scanner(in);
			while (scanner.hasNextLine()) {
				library.append(scanner.nextLine() + "\n");
			}
			in.close();
		} catch (Exception e) {}
		
		return library.toString();
	}
}