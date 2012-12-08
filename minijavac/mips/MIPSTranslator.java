package minijavac.mips;

import java.util.*;
import minijavac.ir.*;
import minijavac.*;

public class MIPSTranslator {
	private SymbolTable symbolTable;
	private MIPSRegisterAllocator registerAllocator;
	private MIPSFrameAllocator frameAllocator;
	private ClassAttribute curClass;
	private MethodAttribute curMethod;
	private HashMap<String, ClassAttribute> classes;
	
	public MIPSTranslator(SymbolTable symTable, MIPSRegisterAllocator regAlloc, MIPSFrameAllocator frameAlloc) {
		symbolTable = symTable;
		registerAllocator = regAlloc;
		frameAllocator = frameAlloc;
		curClass = null;
		curMethod = null;
		storeClasses();
	}

	public void storeClasses() {
		classes = new HashMap<String, ClassAttribute>();
		HashMap<String, LinkedList<Object>> environment = symbolTable.getEnvironment();
		for (Map.Entry<String, LinkedList<Object>> entry : environment.entrySet()) {
			Attribute attr = (Attribute)(entry.getValue().get(0));
			if (attr instanceof ClassAttribute) {
				classes.put(entry.getKey(), (ClassAttribute)attr);
			}
		}
	}

	public MethodAttribute findMethod(String className, String methodName) {
		if (classes.get(className) == null) {
			return null;
		}

		ClassAttribute klass = classes.get(className);
		return klass.getMethod(methodName);
	}

	public Assembly translate(IR ir) {
		Assembly assembly = new MIPSAssembly();

		for (int i = 0; i < ir.size(); i++) {
			MethodIR methodIR = ir.getMethodIR(i);
			
			// adjust the symbol table's scope
			symbolTable.startScope();
			ClassAttribute klass = (ClassAttribute)symbolTable.get(methodIR.getClassName());
			klass.getInMyScope(symbolTable);
			symbolTable.startScope();
			MethodAttribute method = klass.getMethod(methodIR.getMethodName());
			method.getInMyScope(symbolTable);

			curMethod = method;

			for (int j = 0; j < methodIR.size(); j++) {
				Quadruple quad = methodIR.getQuad(j);
				assembly.addInstructions(translateQuad(quad));
			}

			// TODO: is there a better way to check for main?
			if (methodIR.getMethodName().equals("main")) {
				assembly.addInstruction(new Jal("_system_exit"));
			}

			symbolTable.endScope();
			symbolTable.endScope();
		}

		return assembly;
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

		// use proper instructions based on operator
		if (quad.operator.equals("+")) {
			instructions.add(new Add(quad.result, quad.arg1, quad.arg2));
		} else if (quad.operator.equals("-")) {
			instructions.add(new Sub(quad.result, quad.arg1, quad.arg2));
		} else if (quad.operator.equals("*")) {
			instructions.add(new Mult(quad.arg1, quad.arg2));
			instructions.add(new Mflo(quad.result));
		}

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
		/*
		String paramReg = regAllocator.getParamRegister();
		
		if (isInt(quad.arg1)) {
			instructions.add(new Li(paramReg, Integer.parseInt(quad.arg1)));
		} else if (symbolTable.get(quad.arg1) instanceof VariableAttribute) {
			VariableAttribute var = (VariableAttribute)symbolTable.get(quad.arg1);
			instructions.add(new Move(paramReg, var.getRegister()));
		}
		*/
		return instructions;
	}
	private ArrayList<Instruction> translateCall(Quadruple quad) {
		ArrayList<Instruction> instructions = new ArrayList<Instruction>();
		/*
		String fullMethodName = quad.arg1;
		if (fullMethodName.equals("System.out.println")) {
			instructions.add(new Jal("_system_out_println"));
			return instructions;
		}

		String resultReg = regAllocator.getTempRegister();

		String className = fullMethodName.split("\\.")[0];
		String methodName = fullMethodName.split("\\.")[1];
		MethodAttribute method = findMethod(className, methodName);

		instructions.add(new Jal(fullMethodName));
		instructions.add(new Move(resultReg, method.getReturnRegister()));
		// store the register loc of result in the symbol table
		VariableAttribute var = (VariableAttribute)symbolTable.get(quad.result);
		var.setRegister(resultReg);
		*/
		return instructions;
	}
	private ArrayList<Instruction> translateReturn(Quadruple quad) {
		ArrayList<Instruction> instructions = new ArrayList<Instruction>();
		/*
		String returnReg = regAllocator.getReturnRegister();

		if (isInt(quad.arg1)) {
			instructions.add(new Li(returnReg, Integer.parseInt(quad.arg1)));
		} else if (symbolTable.get(quad.arg1) instanceof VariableAttribute) {
			VariableAttribute var = (VariableAttribute)symbolTable.get(quad.arg1);
			instructions.add(new Move(returnReg, var.getRegister()));
		}

		curMethod.setReturnRegister(returnReg);
		*/
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
		/*
		String resultReg = regAllocator.getTempRegister();

		//instructions.add(new Move(resultReg, "$v0"));

		// We don't support objects yet, so store 'null' in the register
		instructions.add(new Li(resultReg, 0));

		// store the register loc of result in the symbol table
		VariableAttribute var = (VariableAttribute)symbolTable.get(quad.result);
		var.setRegister(resultReg);
		*/
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