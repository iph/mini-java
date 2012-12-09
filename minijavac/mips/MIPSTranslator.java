package minijavac.mips;

import java.util.*;
import minijavac.ir.*;
import minijavac.mips.instructions.*;
import minijavac.*;

public class MIPSTranslator {
	private SymbolTable symbolTable;
	private MIPSRegisterAllocator registerAllocator;
	private MIPSFrameAllocator frameAllocator;
	private ClassAttribute curClass;
	private MethodAttribute curMethod;
	private MethodIR curMethodIR;
	private HashMap<String, ClassAttribute> classes;
	private Assembly assembly;

	public MIPSTranslator(SymbolTable symTable, MIPSRegisterAllocator regAlloc, MIPSFrameAllocator frameAlloc) {
		symbolTable = symTable;
		registerAllocator = regAlloc;
		frameAllocator = frameAlloc;
		curClass = null;
		curMethod = null;
		curMethodIR = null;
		storeClasses();
		assembly = null;
	}

	private void storeClasses() {
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
		assembly = new MIPSAssembly();

		for (int i = 0; i < ir.size(); i++) {
			MethodIR methodIR = ir.getMethodIR(i);
			
			// adjust the symbol table's scope
			symbolTable.startScope();
			ClassAttribute klass = (ClassAttribute)symbolTable.get(methodIR.getClassName());
			klass.getInMyScope(symbolTable);
			symbolTable.startScope();
			MethodAttribute method = klass.getMethod(methodIR.getMethodName());
			method.getInMyScope(symbolTable);

			curClass = klass;
			curMethod = method;
			curMethodIR = methodIR;

			boolean inMain = curMethod.getIdentifier().equals("main") ? true : false;

			if (inMain) {
				// this should be 0, but just to be safe in case we had globals..
				int assemblySize = assembly.size();

				int lastAssemblySize = assembly.size();
				for (int j = 0; j < methodIR.size(); j++) {
					Quadruple quad = methodIR.getQuad(j);
					// generate mips code
					translateQuad(quad);

					if (methodIR.hasLabel(quad)) {
						// if the quad had a label, assign that label
						// to the first instruction it generated
						assembly.addLabel(methodIR.getLabel(quad), assembly.getInstruction(lastAssemblySize))
					}
					lastAssemblySize = assembly.size();
				}

				// stick the method label on the first generated instruction
				String label = methodIR.canonicalMethodName();
				assembly.addLabel(label, assembly.getInstruction(assemblySize));
				// only main can exit like this
				assembly.addInstruction(new Jal("_system_exit"));
			} else {
				addPrologue(methodIR);

				int lastAssemblySize = assembly.size();
				for (int j = 0; j < methodIR.size(); j++) {
					Quadruple quad = methodIR.getQuad(j);
					// generate mips code
					translateQuad(quad);

					if (methodIR.hasLabel(quad)) {
						// if the quad had a label, assign that label
						// to the first instruction it generated
						assembly.addLabel(methodIR.getLabel(quad), assembly.getInstruction(lastAssemblySize))
					}
					lastAssemblySize = assembly.size();
				}

				addEpilogue(methodIR);
			}

			symbolTable.endScope();
			symbolTable.endScope();
		}

		return assembly;
	}

	// TODO: preserve saved temporaries, $s0-$s7?
	private void addPrologue(MethodIR methodIR) {
		MIPSFrame frame = frameAllocator.getFrame(methodIR.canonicalMethodName());
		// allocate space for activation frame
		Instruction firstInstr = new Addi("$sp", "$sp", -frame.getSize());
		assembly.addInstruction(firstInstr);
		// slap a label on the first instruction
		assembly.addLabel(methodIR.canonicalMethodName(), firstInstr);
		// save the return address and caller's frame pointer
		assembly.addInstruction(new Sw("$ra", "$sp", frame.getSize()-4));
		assembly.addInstruction(new Sw("$fp", "$sp", frame.getSize()-8));
		// set the frame pointer for convenient access
		assembly.addInstruction(new Addi("$fp", "$sp", frame.getSize()));
	}

	private void addEpilogue(MethodIR methodIR) {
		MIPSFrame frame = frameAllocator.getFrame(methodIR.canonicalMethodName());
		// update return address to what it was
		Instruction firstInstr = new Lw("$ra", "$sp", frame.getSize()-4);
		assembly.addInstruction(firstInstr);
		// slap a label on the first instruction so we can jump to the
		// epilogue if needed (early return)
		assembly.addLabel(methodIR.canonicalMethodName() + "_epilogue", firstInstr);
		// update frame pointer to what it was
		assembly.addInstruction(new Lw("$fp", "$sp", frame.getSize()-8));
		// deallocate activation frame
		assembly.addInstruction(new Addi("$sp", "$sp", frame.getSize()));
		// return to caller
		assembly.addInstruction(new Jr("$ra"));
	}

	// TODO: store register info in symbol table so later quads can
	//       'ask' for the info
	private void translateQuad(Quadruple quad) {
		switch (quad.getType()) {
		case BINARY_ASSIGN:
			translateBinaryAssign(quad);
			break;
		case UNARY_ASSIGN:
			translateUnaryAssign(quad);
			break;
		case COPY:
			translateCopy(quad);
			break;
		case JUMP:
			translateJump(quad);
			break;
		case COND_JUMP:
			translateConditionalJump(quad);
			break;
		case PARAM:
			translateParam(quad);
			break;
		case CALL:
			translateCall(quad);
			break;
		case RETURN:
			translateReturn(quad);
			break;
		case ARRAY_ASSIGN:
			translateArrayAssign(quad);
			break;
		case INDEXED_ASSIGN:
			translateIndexedAssign(quad);
			break;
		case NEW:
			translateNew(quad);
			break;
		case NEW_ARRAY:
			translateNewArray(quad);
			break;
		case LENGTH:
			translateLength(quad);
			break;
		case LOAD:
			translateLoad(quad);
			break;
		case STORE:
			translateStore(quad);
			break;
		}
	}

	private void translateBinaryAssign(Quadruple quad) {
		// use proper instructions based on operator
		if (quad.operator.equals("+")) {
			assembly.addInstruction(new Add(quad.result, quad.arg1, quad.arg2));
		} else if (quad.operator.equals("-")) {
			assembly.addInstruction(new Sub(quad.result, quad.arg1, quad.arg2));
		} else if (quad.operator.equals("*")) {
			assembly.addInstruction(new Mult(quad.arg1, quad.arg2));
			assembly.addInstruction(new Mflo(quad.result));
		}
	}

	private void translateUnaryAssign(Quadruple quad) {
		if (quad.operator.equals("!")) {
			assembly.addInstruction(new Nor(quad.result, quad.arg1, quad.arg1));
		}
	}

	private void translateCopy(Quadruple quad) {
		if (quad.arg1.equals("this")) {
			assembly.addInstruction(new Move(quad.result, "$a0"));
		} else if (quad.arg1.equals("true")) {
			assembly.addInstruction(new Li(quad.result, -1));
		} else if (quad.arg1.equals("false")) {
			assembly.addInstruction(new Li(quad.result, 0));
		} else if (isInt(quad.arg1)) {
			assembly.addInstruction(new Li(quad.result, quad.arg1));
		} else {
			// must be a register
			assembly.addInstruction(new Move(quad.result, quad.arg1));
		}
	}

	private void translateJump(Quadruple quad) {
		assembly.addInstruction(new Jump(quad.arg1));
	}

	private void translateConditionalJump(Quadruple quad) {
		assembly.addInstruction(new Bne(quad.arg2, "$zero", quad.arg1));
	}

	// TODO
	// Old code below
	private void translateParam(Quadruple quad) {
		/*
		String paramReg = regAllocator.getParamRegister();
		
		if (isInt(quad.arg1)) {
			assembly.addInstruction(new Li(paramReg, Integer.parseInt(quad.arg1)));
		} else if (symbolTable.get(quad.arg1) instanceof VariableAttribute) {
			VariableAttribute var = (VariableAttribute)symbolTable.get(quad.arg1);
			assembly.addInstruction(new Move(paramReg, var.getRegister()));
		}
		*/
	}

	private void translateCall(Quadruple quad) {
		String fullMethodName = quad.arg1;
		if (fullMethodName.equals("System.out.println")) {
			assembly.addInstruction(new Jal("_system_out_println"));
			return;
		}

		assembly.addInstruction(new Jal(fullMethodName));
		assembly.addInstruction(new Move(quad.result, "$v0"));
	}

	private void translateReturn(Quadruple quad) {
		assembly.addInstruction(new Move("$v0", quad.arg1));
		// TODO: account for early returns even though they don't exist?
		//assembly.addInstruction(new Jump(curMethodIR.canonicalMethodName() + "_epilogue"));
	}

	private void translateArrayAssign(Quadruple quad) {
	}

	private void translateIndexedAssign(Quadruple quad) {
	}

	private void translateNew(Quadruple quad) {
		/*
		String resultReg = regAllocator.getTempRegister();

		//assembly.addInstruction(new Move(resultReg, "$v0"));

		// We don't support objects yet, so store 'null' in the register
		assembly.addInstruction(new Li(resultReg, 0));

		// store the register loc of result in the symbol table
		VariableAttribute var = (VariableAttribute)symbolTable.get(quad.result);
		var.setRegister(resultReg);
		*/
	}

	private void translateNewArray(Quadruple quad) {
	}

	private void translateLength(Quadruple quad) {
	}

	private void translateLoad(Quadruple quad) {
	}

	private void translateStore(Quadruple quad) {
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