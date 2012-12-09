package minijavac.mips;

import java.util.*;
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
		precolorTransform(ir);

		for (int i = 0; i < ir.size(); i++) {
			MethodIR methodIR = ir.getMethodIR(i);
			// graph color the method's ir to find optimal register use
			RegisterAllocator regAlloc = new RegisterAllocator(methodIR);
			regAlloc.color();
			regAllocators.put(methodIR.canonicalMethodName(), regAlloc);
		}
	}

	private void precolorTransform(IR ir) {
		for (int i = 0; i < ir.size(); i++) {
			MethodIR methodIR = ir.getMethodIR(i);

			// adjust the symbol table's scope
			symbolTable.startScope();
			ClassAttribute klass = (ClassAttribute)symbolTable.get(methodIR.getClassName());
			klass.getInMyScope(symbolTable);
			symbolTable.startScope();
			MethodAttribute method = klass.getMethod(methodIR.getMethodName());
			method.getInMyScope(symbolTable);

            updateFormalDefs(methodIR, method);

			symbolTable.endScope();
			symbolTable.endScope();
		}
	}

    private void updateFormalDefs(MethodIR method, MethodAttribute methodAttrs){
        Quadruple start = new Quadruple(InstructionType.COPY);
        start.result = "this";
        start.arg1 = "$a0";

        method.insertQuad(0, start);

        for (int i = 0; i < methodAttrs.parameterListSize(); i++) {
        	if (i < 3) {
                Quadruple q = new Quadruple(InstructionType.COPY);
                q.result = methodAttrs.getParameter(i).getIdentifier();
                q.arg1 = "$a" + (i + 1);

                method.insertQuad(0, q);
            }
            else{
                Quadruple q = new Quadruple(InstructionType.LOAD);
                q.result = methodAttrs.getParameter(i).getIdentifier();
                q.arg1 = ("$sp");
                q.arg2 = "" + (-8 + (i-4)*4);
                method.insertQuad(0, q);
            }
        }
    }
}
