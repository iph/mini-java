package minijavac.mips;

import java.util.*;
import minijavac.*;
import minijavac.ir.*;

public class MIPSObjectTransformer {
	private SymbolTable symbolTable;
	private ClassAttribute curClass;
	private MethodAttribute curMethod;

	public MIPSObjectTransformer(SymbolTable symTable) {
		symbolTable = symTable;
	}

	public void transformIR(IR ir) {
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

			updateUses(methodIR);
			updateDefs(methodIR);

			symbolTable.endScope();
		}
	}

	// TODO: if there are multiple instructions referencing the same class
	//       var, we probably shouldn't do a load instruction each time.
	//
	private void updateUses(MethodIR methodIR) {
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

	private void updateDefs(MethodIR methodIR) {
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
}