package minijavac.mips;

import minijavac.*;
import minijavac.ir.*;
import minijavac.graph.*;

public class MIPSRegisterAllocator {
	private SymbolTable symbolTable;
	private MIPSFrameAllocator frameAllocator;
	private HashMap<String, RegisterAllocator> regAllocators;

	public MIPSRegisterAllocator(SymbolTable symTable, MIPSFrameAllocator frameAlloc) {
		symbolTable = symTable;
		frameAllocator = frameAllocator;
		regAllocators = new HashMap<String, RegisterAllocator>();
	}

	public void allocate(IR ir) {
		for (int i = 0; i < ir.size(); i++) {
			MethodIR methodIR = ir.getMethodIR(i);
			// graph color the method's ir to find optimal register use
			RegisterAllocator regAlloc = new RegisterAllocator(methodIR);
			regAlloc.color();
			regAllocators.put(methodIR.canonicalMethodName(), regAlloc);
		}
	}

	public String getReservedRegister(String canonicalMethodName) {
		//return regAllocators.get(canonicalMethodName).getReservedRegister();
		return "$t9";
	}
}