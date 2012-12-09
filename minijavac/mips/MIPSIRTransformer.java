package minijavac.mips;

import java.util.*;
import minijavac.*;
import minijavac.ir.*;

public class MIPSIRTransformer {
	private SymbolTable symbolTable;
	private ClassAttribute curClass;
	private MethodAttribute curMethod;
	private HashMap<String, ClassAttribute> classes;

	public MIPSIRTransformer(SymbolTable symTable) {
		symbolTable = symTable;
		storeClasses();
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

	public void transform(IR ir) {
		for (int i = 0; i < ir.size(); i++) {
			MethodIR methodIR = ir.getMethodIR(i);

			// adjust the symbol table's scope
			symbolTable.startScope();
			ClassAttribute klass = (ClassAttribute)symbolTable.get(methodIR.getClassName());
			klass.getInMyScope(symbolTable);
			symbolTable.startScope();
			MethodAttribute method = klass.getMethod(methodIR.getMethodName());
            updateFormalDefs(methodIR, method);
			method.getInMyScope(symbolTable);

			curClass = klass;
			curMethod = method;

			updateClassVarUses(methodIR);
			updateClassVarDefs(methodIR);

			updateArrayUses(methodIR);
			updateArrayDefs(methodIR);

			updateNewArrays(methodIR);
			updateNewObjects(methodIR);

			symbolTable.endScope();
			symbolTable.endScope();
		}
	}

    private void updateFormalDefs(MethodIR method, MethodAttribute methodAttrs){
        for(int i = 0; i < methodAttrs.parameterListSize(); i++){
            if( i < 4){
                Quadruple q = new Quadruple(InstructionType.COPY);
                q.result = methodAttrs.getParameter(i).getIdentifier();
                q.arg1 = "$a" + (i + 1);

                method.insertQuad(0, q);
            }
        }
    }
	private boolean isClassVar(String identifier) {
		Object var = symbolTable.get(identifier);
		return (var != null
				&& var instanceof VariableAttribute
				&& !curMethod.hasVariable(identifier)
				&& !curMethod.hasParameter(identifier));
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
				if (isClassVar(quad.arg1))
					vars.add((VariableAttribute)arg1Symbol);
				if (isClassVar(quad.arg2))
					vars.add((VariableAttribute)arg2Symbol);
				break;
			case UNARY_ASSIGN:
				if (isClassVar(quad.arg1))
					vars.add((VariableAttribute)arg1Symbol);
				break;
			case COPY:
				if (isClassVar(quad.arg1))
					vars.add((VariableAttribute)arg1Symbol);
				break;
			case RETURN:
				if (isClassVar(quad.arg1))
					vars.add((VariableAttribute)arg1Symbol);
				break;
			case ARRAY_ASSIGN:
				if (isClassVar(quad.arg1))
					vars.add((VariableAttribute)arg1Symbol);
				if (isClassVar(quad.arg2))
					vars.add((VariableAttribute)arg2Symbol);
				break;
			case INDEXED_ASSIGN:
				if (isClassVar(quad.arg1))
					vars.add((VariableAttribute)arg1Symbol);
				if (isClassVar(quad.arg2))
					vars.add((VariableAttribute)arg2Symbol);
				break;
			case LENGTH:
				if (isClassVar(quad.arg1))
					vars.add((VariableAttribute)arg1Symbol);
				break;
			}

			// did we have any class vars in the quad that need to have a load
			// instruction added?
			if (vars.size() > 0) {
				ArrayList<Quadruple> newQuads = new ArrayList<Quadruple>();
				for (int j = 0; j < vars.size(); j++) {
					String tempVar = methodIR.nextTempVar();
					curMethod.addVariable(tempVar, new VariableAttribute(tempVar, curClass.getIdentifier()));

					Quadruple thisQuad = new Quadruple(InstructionType.COPY);
					thisQuad.operator = ":=";
					thisQuad.result = tempVar;
					thisQuad.arg1 = "this";

					VariableAttribute var = (VariableAttribute)vars.get(j);
					// add a quad so we can access the member var
					Quadruple loadQuad = new Quadruple(InstructionType.LOAD);
					loadQuad.operator = "load";
					loadQuad.result = var.getIdentifier();
					loadQuad.arg1 = tempVar;
					loadQuad.arg2 = ""+var.getStorage().getOffset();

					newQuads.add(thisQuad);
					newQuads.add(loadQuad);
				}
				// add the old quad (that uses the class var) last
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
				if (isClassVar(quad.result))
					var = (VariableAttribute)resultSymbol;
				break;
			}

			// did we have a class var in the quad that needs to have a store
			// instruction added?
			if (var != null) {
				ArrayList<Quadruple> newQuads = new ArrayList<Quadruple>();
				// create a temp var that now holds the result of the instruction
				// instead of our class var
				String tempVar1 = methodIR.nextTempVar();
				curMethod.addVariable(tempVar1, new VariableAttribute(tempVar1, var.getType()));
				// update the old quad to store the result in temp var instead of the class var
				quad.result = tempVar1;

				String tempVar2 = methodIR.nextTempVar();
				curMethod.addVariable(tempVar2, new VariableAttribute(tempVar2, curClass.getIdentifier()));

				Quadruple thisQuad = new Quadruple(InstructionType.COPY);
				thisQuad.operator = ":=";
				thisQuad.result = tempVar2;
				thisQuad.arg1 = "this";

				// add a quad so we can store at the member var location
				Quadruple storeQuad = new Quadruple(InstructionType.STORE);
				storeQuad.operator = "store";
				storeQuad.result = tempVar2;
				storeQuad.arg1 = tempVar1;
				storeQuad.arg2 = ""+var.getStorage().getOffset();

				// add the old quad we updated, first
				newQuads.add(quad);
				newQuads.add(thisQuad);
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

	private void updateNewArrays(MethodIR methodIR) {
		// result := new arg1[], arg2
		//
		// becomes:
		// newTemp1 := 1
		// newTemp2 := 4
		// newTemp3 := arg2 + newTemp1
		// newTemp4 := newTemp3 * newTemp2
		// param newTemp4
		// result := call _new_array, 1

		int irSize = methodIR.size();
		for (int i = irSize-1; i >= 0; i--) {
			Quadruple quad = methodIR.getQuad(i);
			if (quad.getType() != InstructionType.NEW_ARRAY) {
				continue;
			}

			ArrayList<Quadruple> newQuads = new ArrayList<Quadruple>();

			// we'll need 4 new temporaries to calculate array size
			String tempVar1 = methodIR.nextTempVar();
			String tempVar2 = methodIR.nextTempVar();
			String tempVar3 = methodIR.nextTempVar();
			String tempVar4 = methodIR.nextTempVar();
			curMethod.addVariable(tempVar1, new VariableAttribute(tempVar1, "int"));
			curMethod.addVariable(tempVar2, new VariableAttribute(tempVar2, "int"));
			curMethod.addVariable(tempVar3, new VariableAttribute(tempVar3, "int"));
			curMethod.addVariable(tempVar4, new VariableAttribute(tempVar4, "int"));

			// we need to first store our length constant (= 1 int)
			Quadruple lengthConstQuad = new Quadruple(InstructionType.COPY);
			lengthConstQuad.operator = ":=";
			lengthConstQuad.result = tempVar1;
			lengthConstQuad.arg1 = "1";

			// then we need to store our scaling constant (= 4 bytes to an int)
			Quadruple scaleConstQuad = new Quadruple(InstructionType.COPY);
			scaleConstQuad.operator = ":=";
			scaleConstQuad.result = tempVar2;
			scaleConstQuad.arg1 = "4";

			// then add the amount of ints + length constant
			Quadruple numIntsQuad = new Quadruple(InstructionType.BINARY_ASSIGN);
			numIntsQuad.operator = "+";
			numIntsQuad.result = tempVar3;
			numIntsQuad.arg1 = quad.arg2;
			numIntsQuad.arg2 = tempVar1;

			// then we scale by the size of ints
			Quadruple scaleQuad = new Quadruple(InstructionType.BINARY_ASSIGN);
			scaleQuad.operator = "*";
			scaleQuad.result = tempVar4;
			scaleQuad.arg1 = tempVar3;
			scaleQuad.arg2 = tempVar2;

			// next, we pass our temp holding the size of the array
			// as a param to the _new_array function
			Quadruple paramQuad = new Quadruple(InstructionType.PARAM);
			paramQuad.operator = "param";
			paramQuad.arg1 = tempVar4;

			// finally, we call the allocation function
			Quadruple callQuad = new Quadruple(InstructionType.CALL);
			callQuad.operator = "call";
			callQuad.result = quad.result;
			callQuad.arg1 = "_new_array";
			callQuad.arg2 = "1";

			// stick all the quads in an array and replace the old quad
			newQuads.add(lengthConstQuad);
			newQuads.add(scaleConstQuad);
			newQuads.add(numIntsQuad);
			newQuads.add(scaleQuad);
			newQuads.add(paramQuad);
			newQuads.add(callQuad);

			methodIR.replaceQuadAt(i, newQuads);
		}
	}

	private void updateNewObjects(MethodIR methodIR) {
		// result := new arg1
		//
		// becomes:
		// newTemp1 := classSize(arg1)
		// param newTemp1
		// result := call _new_object, 1

		int irSize = methodIR.size();
		for (int i = irSize-1; i >= 0; i--) {
			Quadruple quad = methodIR.getQuad(i);
			if (quad.getType() != InstructionType.NEW) {
				continue;
			}

			ArrayList<Quadruple> newQuads = new ArrayList<Quadruple>();

			// we'll need a new temporary to store class size
			String tempVar1 = methodIR.nextTempVar();
			curMethod.addVariable(tempVar1, new VariableAttribute(tempVar1, "int"));

			// we need to first store our class size (in bytes)
			Quadruple sizeQuad = new Quadruple(InstructionType.COPY);
			sizeQuad.operator = ":=";
			sizeQuad.result = tempVar1;
			sizeQuad.arg1 = ""+classes.get(quad.arg1).getSize();

			// next, we pass our temp holding the size of the class
			// as a param to the _new_object function
			Quadruple paramQuad = new Quadruple(InstructionType.PARAM);
			paramQuad.operator = "param";
			paramQuad.arg1 = tempVar1;

			// finally, we call the allocation function
			Quadruple callQuad = new Quadruple(InstructionType.CALL);
			callQuad.operator = "call";
			callQuad.result = quad.result;
			callQuad.arg1 = "_new_object";
			callQuad.arg2 = "1";

			// stick all the quads in an array and replace the old quad
			newQuads.add(sizeQuad);
			newQuads.add(paramQuad);
			newQuads.add(callQuad);

			methodIR.replaceQuadAt(i, newQuads);
		}
	}
}
