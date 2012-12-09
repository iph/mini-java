package minijavac.mips;

import java.util.*;
import minijavac.*;
import minijavac.ir.*;

public class MIPSIRTransformer {
	private SymbolTable symbolTable;
	private ClassAttribute curClass;
	private MethodAttribute curMethod;

	public MIPSIRTransformer(SymbolTable symTable) {
		symbolTable = symTable;
	}

	public void transform(IR ir) {
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

			updateClassVarUses(methodIR);
			updateClassVarDefs(methodIR);

			updateArrayUses(methodIR);
			updateArrayDefs(methodIR);

			symbolTable.endScope();
		}
	}

	// TODO: if there are multiple instructions referencing the same class
	//       var, we probably shouldn't do a load instruction each time.
	//
	private void updateClassVarUses(MethodIR methodIR) {
		int irSize = methodIR.size();
		for (int i = irSize-1; i >= 0; i--) {
			Quadruple quad = methodIR.getQuad(i);

			Object arg1Symbol = symbolTable.get(quad.arg1);
			Object arg2Symbol = symbolTable.get(quad.arg2);

			ArrayList<VariableAttribute> vars = new ArrayList<VariableAttribute>();

			switch (quad.getType()) {
			case BINARY_ASSIGN:
				if (arg1Symbol != null && arg1Symbol instanceof VariableAttribute 
					&& !curMethod.hasVariable(quad.arg1))
					vars.add((VariableAttribute)arg1Symbol);
				if (arg2Symbol != null && arg2Symbol instanceof VariableAttribute
					&& !curMethod.hasVariable(quad.arg1))
					vars.add((VariableAttribute)arg2Symbol);
				break;
			case UNARY_ASSIGN:
				if (arg1Symbol != null && arg1Symbol instanceof VariableAttribute
					&& !curMethod.hasVariable(quad.arg1))
					vars.add((VariableAttribute)arg1Symbol);
				break;
			case COPY:
				if (arg1Symbol != null && arg1Symbol instanceof VariableAttribute
					&& !curMethod.hasVariable(quad.arg1))
					vars.add((VariableAttribute)arg1Symbol);
				break;
			case RETURN:
				if (arg1Symbol != null && arg1Symbol instanceof VariableAttribute
					&& !curMethod.hasVariable(quad.arg1))
					vars.add((VariableAttribute)arg1Symbol);
				break;
			case ARRAY_ASSIGN:
				if (arg1Symbol != null && arg1Symbol instanceof VariableAttribute
					&& !curMethod.hasVariable(quad.arg1))
					vars.add((VariableAttribute)arg1Symbol);
				if (arg2Symbol != null && arg2Symbol instanceof VariableAttribute
					&& !curMethod.hasVariable(quad.arg2))
					vars.add((VariableAttribute)arg2Symbol);
				break;
			case INDEXED_ASSIGN:
				if (arg1Symbol != null && arg1Symbol instanceof VariableAttribute
					&& !curMethod.hasVariable(quad.arg1))
					vars.add((VariableAttribute)arg1Symbol);
				if (arg2Symbol != null && arg2Symbol instanceof VariableAttribute
					&& !curMethod.hasVariable(quad.arg2))
					vars.add((VariableAttribute)arg2Symbol);
				break;
			case LENGTH:
				if (arg1Symbol != null && arg1Symbol instanceof VariableAttribute
					&& !curMethod.hasVariable(quad.arg1))
					vars.add((VariableAttribute)arg1Symbol);
				break;
			}

			// did we have any class vars in the quad that need to have a load
			// instruction added?
			if (vars.size() > 0) {
				ArrayList<Quadruple> newQuads = new ArrayList<Quadruple>();
				for (int j = 0; j < vars.size(); j++) {
					VariableAttribute var = (VariableAttribute)vars.get(j);
					// add a quad so we can access the member var
					Quadruple loadQuad = new Quadruple(InstructionType.LOAD);
					loadQuad.operator = "load";
					loadQuad.result = var.getIdentifier();
					loadQuad.arg1 = "this";
					loadQuad.arg2 = ""+var.getStorage().getOffset();

					newQuads.add(loadQuad);
				}
				// add the old quad second
				newQuads.add(quad);

				// update the method IR to reflect this
				methodIR.replaceQuadAt(i, newQuads);
			}
		}
	}

	private void updateClassVarDefs(MethodIR methodIR) {
		int irSize = methodIR.size();
		for (int i = irSize-1; i >= 0; i--) {
			Quadruple quad = methodIR.getQuad(i);

			Object resultSymbol = symbolTable.get(quad.result);

			VariableAttribute var = null;

			switch (quad.getType()) {
			case BINARY_ASSIGN:
			case UNARY_ASSIGN:
			case COPY:
			case CALL:
			case ARRAY_ASSIGN:
			case INDEXED_ASSIGN:
			case NEW:
			case NEW_ARRAY:
			case LENGTH:
				if (resultSymbol != null && resultSymbol instanceof VariableAttribute
					&& !curMethod.hasVariable(quad.result))
					var = (VariableAttribute)resultSymbol;
				break;
			}

			// did we have a class var in the quad that needs to have a store
			// instruction added?
			if (var != null) {
				ArrayList<Quadruple> newQuads = new ArrayList<Quadruple>();
				// create a temp var that holds the result of the instruction
				// instead of our class var
				String tempVar = methodIR.nextTempVar();
				curMethod.addVariable(tempVar, new VariableAttribute(tempVar, var.getType()));
				// update the old quad to use the temp
				quad.result = tempVar;

				// add a quad so we can store at the member var location
				Quadruple storeQuad = new Quadruple(InstructionType.STORE);
				storeQuad.operator = "store";
				storeQuad.result = "this";
				storeQuad.arg1 = tempVar;
				storeQuad.arg2 = ""+var.getStorage().getOffset();

				// add the old quad we updated, first
				newQuads.add(quad);
				// add the quad storing the result from the first
				newQuads.add(storeQuad);

				methodIR.replaceQuadAt(i, newQuads);
			}
		}
	}

	// TODO: these methods don't have to assume the only arrays
	//       are of ints, but they currently do
	private void updateArrayUses(MethodIR methodIR) {
		// result := arg1[arg2]
		//
		// newTemp1 := 4
		// newTemp2 := arg2 * newTemp1
		// newTemp3 := newTemp2 + newTemp1 (length is first WORD)
		// result := load arg1 + offset newTemp3

		int irSize = methodIR.size();
		for (int i = irSize-1; i >= 0; i--) {
			Quadruple quad = methodIR.getQuad(i);
			if (quad.getType() != InstructionType.ARRAY_ASSIGN) {
				continue;
			}

			ArrayList<Quadruple> newQuads = new ArrayList<Quadruple>();

			// we'll need 3 new temporaries to do this array access
			String tempVar1 = methodIR.nextTempVar();
			String tempVar2 = methodIR.nextTempVar();
			String tempVar3 = methodIR.nextTempVar();
			curMethod.addVariable(tempVar1, new VariableAttribute(tempVar1, "int"));
			curMethod.addVariable(tempVar2, new VariableAttribute(tempVar2, "int"));
			curMethod.addVariable(tempVar3, new VariableAttribute(tempVar3, "int"));

			// we need to first store our scaling constant (4 for an int)
			Quadruple constQuad = new Quadruple(InstructionType.COPY);
			constQuad.operator = ":=";
			constQuad.result = tempVar1;
			constQuad.arg1 = "4";

			// then multiply the scaling constant by our index
			Quadruple scaleQuad = new Quadruple(InstructionType.BINARY_ASSIGN);
			scaleQuad.operator = "*";
			scaleQuad.result = tempVar2;
			scaleQuad.arg1 = quad.arg2;
			scaleQuad.arg2 = tempVar1;

			// then we have to jump past the length WORD at the beginning
			Quadruple skipQuad = new Quadruple(InstructionType.BINARY_ASSIGN);
			skipQuad.operator = "+";
			skipQuad.result = tempVar3;
			skipQuad.arg1 = tempVar2;
			skipQuad.arg2 = tempVar1;

			// finally, we can load the proper index into our register
			Quadruple loadQuad = new Quadruple(InstructionType.STORE);
			loadQuad.operator = "load";
			loadQuad.result = quad.result;
			loadQuad.arg1 = quad.arg1;
			loadQuad.arg2 = tempVar3;

			// stick all the quads in an array and replace the old quad
			newQuads.add(constQuad);
			newQuads.add(scaleQuad);
			newQuads.add(skipQuad);
			newQuads.add(loadQuad);

			methodIR.replaceQuadAt(i, newQuads);
		}
	}

	private void updateArrayDefs(MethodIR methodIR) {
		// result[arg1] := arg2
		//
		// becomes:
		// newTemp1 := 4
		// newTemp2 := arg1 * newTemp1
		// newTemp3 := newTemp2 + newTemp1 (length is first WORD)
		// store arg2, result + offset newTemp3

		int irSize = methodIR.size();
		for (int i = irSize-1; i >= 0; i--) {
			Quadruple quad = methodIR.getQuad(i);
			if (quad.getType() != InstructionType.ARRAY_ASSIGN) {
				continue;
			}

			ArrayList<Quadruple> newQuads = new ArrayList<Quadruple>();

			// we'll need 3 new temporaries to do this array access
			String tempVar1 = methodIR.nextTempVar();
			String tempVar2 = methodIR.nextTempVar();
			String tempVar3 = methodIR.nextTempVar();
			curMethod.addVariable(tempVar1, new VariableAttribute(tempVar1, "int"));
			curMethod.addVariable(tempVar2, new VariableAttribute(tempVar2, "int"));
			curMethod.addVariable(tempVar3, new VariableAttribute(tempVar3, "int"));

			// we need to first store our scaling constant (4 for an int)
			Quadruple constQuad = new Quadruple(InstructionType.COPY);
			constQuad.operator = ":=";
			constQuad.result = tempVar1;
			constQuad.arg1 = "4";

			// then multiply the scaling constant by our index
			Quadruple scaleQuad = new Quadruple(InstructionType.BINARY_ASSIGN);
			scaleQuad.operator = "*";
			scaleQuad.result = tempVar2;
			scaleQuad.arg1 = quad.arg1;
			scaleQuad.arg2 = tempVar1;

			// then we have to jump past the length WORD at the beginning
			Quadruple skipQuad = new Quadruple(InstructionType.BINARY_ASSIGN);
			skipQuad.operator = "+";
			skipQuad.result = tempVar3;
			skipQuad.arg1 = tempVar2;
			skipQuad.arg2 = tempVar1;

			// finally, we can store at the proper index
			Quadruple storeQuad = new Quadruple(InstructionType.STORE);
			storeQuad.operator = "store";
			storeQuad.result = quad.result;
			storeQuad.arg1 = quad.arg2;
			storeQuad.arg2 = tempVar3;

			// stick all the quads in an array and replace the old quad
			newQuads.add(constQuad);
			newQuads.add(scaleQuad);
			newQuads.add(skipQuad);
			newQuads.add(storeQuad);

			methodIR.replaceQuadAt(i, newQuads);
		}
	}
}