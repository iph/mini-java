package minijavac.mips;

import java.util.*;
import minijavac.SymbolTable;
import minijavac.ir.*;
import minijavac.ClassAttribute;
import minijavac.MethodAttribute;
import minijavac.VariableAttribute;

public class MIPSTranslator {
	private SymbolTable symbolTable;
	private MIPSRegisterAllocator regAllocator;

	public MIPSTranslator(SymbolTable symTable) {
		symbolTable = symTable;
		regAllocator = new MIPSRegisterAllocator();
	}

	public ArrayList<Instruction> translate(IR ir) {
		ArrayList<Instruction> instructions = new ArrayList<Instruction>();
		
		for (int i = 0; i < ir.size(); i++) {
			MethodIR methodIR = ir.getMethodIR(i);
			
			// adjust the symbol table's scope
			symbolTable.startScope();
			ClassAttribute klass = (ClassAttribute)symbolTable.get(methodIR.getClassName());
			klass.getInMyScope(symbolTable);
			symbolTable.startScope();
			MethodAttribute method = klass.getMethod(methodIR.getMethodName());
			method.getInMyScope(symbolTable);

			for (int j = 0; j < methodIR.size(); j++) {
				Quadruple quad = methodIR.getQuad(j);
				instructions.addAll(translateQuad(quad));
			}

			// TODO: is there a better way to check for main?
			if (methodIR.getMethodName().equals("main")) {
				instructions.add(new Jal("_system_exit"));
			}

			symbolTable.endScope();
			symbolTable.endScope();
		}

		return instructions;
	}

	// TODO: store register info in symbol table so later quads can
	//       'ask' for the info
	public ArrayList<Instruction> translateQuad(Quadruple quad) {
		switch (quad.getType()) {
		case BINARY_ASSIGN:
			return translateBinaryAssign(quad);
		case UNARY_ASSIGN:
			return translateUnaryAssign(quad);
		case COPY:
			return translateCopy(quad);
		case JUMP:
			return translateJump(quad);
		case COND_JUMP:
			return translateConditionalJump(quad);
		case PARAM:
			return translateParam(quad);
		case CALL:
			return translateCall(quad);
		case RETURN:
			return translateReturn(quad);
		case ARRAY_ASSIGN:
			return translateArrayAssign(quad);
		case INDEXED_ASSIGN:
			return translateIndexedAssign(quad);
		case NEW:
			return translateNew(quad);
		case NEW_ARRAY:
			return translateNewArray(quad);
		case LENGTH:
			return translateLength(quad);
		}

		return new ArrayList<Instruction>();
	}

	private ArrayList<Instruction> translateBinaryAssign(Quadruple quad) {
		ArrayList<Instruction> instructions = new ArrayList<Instruction>();
		String reg1 = regAllocator.getTempRegister();
		String reg2 = regAllocator.getTempRegister();
		
		instructions.add(new Li(reg1, Integer.parseInt(quad.arg1)));
		instructions.add(new Li(reg2, Integer.parseInt(quad.arg2)));
		
		String reg3 = regAllocator.getTempRegister();
		// use proper instructions based on operator
		if (quad.operator.equals("+")) {
			instructions.add(new Add(reg3, reg1, reg2));
		} else if (quad.operator.equals("-")) {
			instructions.add(new Sub(reg3, reg1, reg2));
		} else if (quad.operator.equals("*")) {
			instructions.add(new Mult(reg1, reg2));
			instructions.add(new Mflo(reg3));
		}
		// store the register loc of result in the symbol table
		VariableAttribute var = (VariableAttribute)symbolTable.get(quad.result);
		var.setRegister(reg3);

		return instructions;
	}
	private ArrayList<Instruction> translateUnaryAssign(Quadruple quad) {
		ArrayList<Instruction> instructions = new ArrayList<Instruction>();
		// TODO
		return instructions;
	}
	private ArrayList<Instruction> translateCopy(Quadruple quad) {
		ArrayList<Instruction> instructions = new ArrayList<Instruction>();
		// TODO
		return instructions;
	}
	private ArrayList<Instruction> translateJump(Quadruple quad) {
		ArrayList<Instruction> instructions = new ArrayList<Instruction>();
		// TODO
		return instructions;
	}
	private ArrayList<Instruction> translateConditionalJump(Quadruple quad) {
		ArrayList<Instruction> instructions = new ArrayList<Instruction>();
		// TODO
		return instructions;
	}
	private ArrayList<Instruction> translateParam(Quadruple quad) {
		ArrayList<Instruction> instructions = new ArrayList<Instruction>();
		String paramReg = regAllocator.getParamRegister();
		
		if (isInt(quad.arg1)) {
			instructions.add(new Li(paramReg, Integer.parseInt(quad.arg1)));
		} else if (symbolTable.get(quad.arg1) instanceof VariableAttribute) {
			VariableAttribute var = (VariableAttribute)symbolTable.get(quad.arg1);
			instructions.add(new Move(paramReg, var.getRegister()));
		}

		return instructions;
	}
	private ArrayList<Instruction> translateCall(Quadruple quad) {
		ArrayList<Instruction> instructions = new ArrayList<Instruction>();
		String fullMethodName = quad.arg1;
		if (fullMethodName.equals("System.out.println")) {
			instructions.add(new Jal("_system_out_println"));
			return instructions;
		}

		String className = fullMethodName.split(".")[0];
		String methodName = fullMethodName.split(".")[1];
		MethodAttribute method = (MethodAttribute)symbolTable.get(methodName);
		// TODO: check if method is in className?
		return instructions;
	}
	private ArrayList<Instruction> translateReturn(Quadruple quad) {
		ArrayList<Instruction> instructions = new ArrayList<Instruction>();
		// TODO
		return instructions;
	}
	private ArrayList<Instruction> translateArrayAssign(Quadruple quad) {
		ArrayList<Instruction> instructions = new ArrayList<Instruction>();
		// TODO
		return instructions;
	}
	private ArrayList<Instruction> translateIndexedAssign(Quadruple quad) {
		ArrayList<Instruction> instructions = new ArrayList<Instruction>();
		// TODO
		return instructions;
	}
	private ArrayList<Instruction> translateNew(Quadruple quad) {
		ArrayList<Instruction> instructions = new ArrayList<Instruction>();
		// TODO
		return instructions;
	}
	private ArrayList<Instruction> translateNewArray(Quadruple quad) {
		ArrayList<Instruction> instructions = new ArrayList<Instruction>();
		// TODO
		return instructions;
	}
	private ArrayList<Instruction> translateLength(Quadruple quad) {
		ArrayList<Instruction> instructions = new ArrayList<Instruction>();
		// TODO
		return instructions;
	}

    private boolean isInt(String possibleInt){
        try{
            Integer.parseInt(possibleInt);
            return true;
        }catch(Exception e){
            return false;
        }
    }
}