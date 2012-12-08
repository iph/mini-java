package minijavac.mips;

import minijavac.*;
import minijavac.ir.*;

public class MIPSRegisterAllocator {
	private SymbolTable symbolTable;
	private MIPSFrameAllocator frameAllocator;
	
	public MIPSRegisterAllocator(SymbolTable symTable, MIPSFrameAllocator frameAlloc) {
		symbolTable = symTable;
		frameAllocator = frameAllocator;
	}

	public void allocate(IR ir) {
	}

	public String getReservedRegister() {
		return "$t0";
	}
}