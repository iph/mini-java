import java.util.*;
import syntaxtree.*;
import ir.*;
import visitor.IRVisitor;

public class IRBuilder implements IRVisitor {
	private SymbolTable symbolTable;
	private HashMap<String, ClassAttribute> classes;
	private HashMap<String, MethodIR> ir;
	private MethodIR curMethodIR;
	// FIXME: shouldn't need to keep track of this
	private String curClass;

	public IRBuilder(SymbolTable symTable) {
		symbolTable = symTable;
		classes = new HashMap<String, ClassAttribute>();
		storeClasses();
		ir = new HashMap<String, MethodIR>();
		curMethodIR = null;
		curClass = "";
	}

	public void storeClasses() {
		HashMap<String, LinkedList<Object>> environment = symbolTable.getEnvironment();
		for (Map.Entry<String, LinkedList<Object>> entry : environment.entrySet()) {
			Attribute attr = (Attribute)(entry.getValue().get(0));
			if (attr instanceof ClassAttribute) {
				classes.put(entry.getKey(), (ClassAttribute)attr);
			}
		}
	}

	public void visit(Program n) {
	    n.m.accept(this);
	    for (int i = 0; i < n.cl.size(); i++) {
	        n.cl.elementAt(i).accept(this);
	    }
	}

	public void visit(MainClass n) {
		String className = n.i1.s;
		symbolTable.startScope();
        ClassAttribute cls = (ClassAttribute)symbolTable.get(className);
        cls.getInMyScope(symbolTable);
        symbolTable.startScope();
		MethodAttribute method = (MethodAttribute)symbolTable.get("main");
		method.getInMyScope(symbolTable);

		// use fully-qualified method name for IR
		MethodIR methodIR = new MethodIR(className + ".main");
		curMethodIR = methodIR;
		// convert statement to IR
    	n.s.accept(this);

    	System.out.println(curMethodIR);

    	symbolTable.endScope();
    	symbolTable.endScope();
	}

	public void visit(ClassDeclSimple n) {
        symbolTable.startScope();
        ClassAttribute cls = (ClassAttribute)symbolTable.get(n.i.s);
        cls.getInMyScope(symbolTable);

        curClass = cls.getIdentifier();

  		for (int i = 0; i < n.ml.size(); i++) {
  			n.ml.elementAt(i).accept(this);
  		}

  		symbolTable.endScope();
	}

	public void visit(ClassDeclExtends n) {
        symbolTable.startScope();
        ClassAttribute cls = (ClassAttribute)symbolTable.get(n.i.s);
        cls.getInMyScope(symbolTable);

        curClass = cls.getIdentifier();

  		for (int i = 0; i < n.ml.size(); i++) {
  			n.ml.elementAt(i).accept(this);
  		}

  		symbolTable.endScope();
	}

	public void visit(VarDecl n) {}

	public void visit(MethodDecl n) {
        symbolTable.startScope();
        MethodAttribute method = (MethodAttribute)symbolTable.get(n.i.s);
        method.getInMyScope(symbolTable);

        // use fully-qualified method name for IR
		MethodIR methodIR = new MethodIR(curClass + "." + n.i.s);
		curMethodIR = methodIR;
		// convert each statement into IR
  		for (int i = 0; i < n.sl.size(); i++) {
  			n.sl.elementAt(i).accept(this);
  		}
		// add IR for return statement
		Quadruple ins = new Quadruple(InstructionType.RETURN);
		ins.operator = "return";
		ins.arg1 = n.e.accept(this);
		curMethodIR.addQuad(ins);

		// resolve all our labels to quads now
		curMethodIR.backpatch();

		System.out.println(curMethodIR);

  		symbolTable.endScope();
	}

	public void visit(Formal n) {}
	public void visit(IntArrayType n) {}
	public void visit(BooleanType n) {}
	public void visit(IntegerType n) {}
	public void visit(IdentifierType n) {}

	public void visit(Block n) {
  		for (int i = 0; i < n.sl.size(); i++) {
  			n.sl.elementAt(i).accept(this);
  		}
	}

	public void visit(If n) {
		// generate code for calculating expression
		String expression = n.e.accept(this);

		Quadruple ins = new Quadruple(InstructionType.COND_JUMP);
		ins.operator = "iftrue";
		String l1 = Quadruple.nextLabel();
		ins.arg1 = l1;
		ins.arg2 = expression;
		curMethodIR.addQuad(ins);

		// generate else code
		n.s2.accept(this);

		// jump from end of else to endif
		ins = new Quadruple(InstructionType.JUMP);
		ins.operator = "goto";
		String l2 = Quadruple.nextLabel();
		ins.arg1 = l2;
		curMethodIR.addQuad(ins);

		// l1 should resolve to the quad right after this goto
		curMethodIR.addFutureLabel(l1, curMethodIR.size());

		// generate if code
		n.s1.accept(this);

		// l2 should resolve to the quad right after this
		curMethodIR.addFutureLabel(l2, curMethodIR.size());
	}

	public void visit(While n) {
		String l1 = Quadruple.nextLabel();
		// l1 should resolve to the first quad of the expression
		curMethodIR.addFutureLabel(l1, curMethodIR.size());
		String expression = n.e.accept(this);

		Quadruple ins = new Quadruple(InstructionType.COND_JUMP);
		ins.operator = "iftrue";
		String l2 = Quadruple.nextLabel();
		ins.arg1 = l2;
		ins.arg2 = expression;
		curMethodIR.addQuad(ins);

		// jump to end of while
		ins = new Quadruple(InstructionType.JUMP);
		ins.operator = "goto";
		String l3 = Quadruple.nextLabel();
		ins.arg1 = l3;
		curMethodIR.addQuad(ins);

		// l2 should resolve to (goto L3) + 1
		curMethodIR.addFutureLabel(l2, curMethodIR.size());

		// generate statement code
		n.s.accept(this);

		// jump to beginning of while
		ins = new Quadruple(InstructionType.JUMP);
		ins.operator = "goto";
		ins.arg1 = l1;
		curMethodIR.addQuad(ins);

		// l3 should resolve to the quad right after this
		curMethodIR.addFutureLabel(l3, curMethodIR.size());
	}

	public void visit(Print n) {
		Quadruple ins = new Quadruple(InstructionType.PARAM);
		ins.operator = "param";
		ins.arg1 = n.e.accept(this);
		curMethodIR.addQuad(ins);
		ins = new Quadruple(InstructionType.CALL);
		ins.operator = "call";
		ins.arg1 = "System.out.println";
		// System.out.println takes 1 param (an int)
		ins.arg2 = ""+1;
		// TODO: should we pretend like it has a result, and ignore it?
		curMethodIR.addQuad(ins);
	}

	public void visit(Assign n) {
		Quadruple ins = new Quadruple(InstructionType.COPY);
		ins.operator = ":=";
		ins.result = n.i.accept(this);
		ins.arg1 = n.e.accept(this);
		curMethodIR.addQuad(ins);
	}

	public void visit(ArrayAssign n) {
		Quadruple ins = new Quadruple(InstructionType.ARRAY_ASSIGN);
		ins.operator = "[]";
		ins.result = n.i.accept(this);
		ins.arg1 = n.e1.accept(this);
		ins.arg2 = n.e2.accept(this);
		curMethodIR.addQuad(ins);
	}

	public String visit(And n) {
		Quadruple ins = new Quadruple(InstructionType.BINARY_ASSIGN);
		ins.operator = "&&";
		ins.arg1 = n.e1.accept(this);
		ins.arg2 = n.e2.accept(this);
		ins.result = Quadruple.nextTempVar();
		symbolTable.put(ins.result, new TempVarAttribute(ins.result, "boolean"));
		curMethodIR.addQuad(ins);
		return ins.result;
	}

	public String visit(LessThan n) {
		Quadruple ins = new Quadruple(InstructionType.BINARY_ASSIGN);
		ins.operator = "<";
		ins.arg1 = n.e1.accept(this);
		ins.arg2 = n.e2.accept(this);
		ins.result = Quadruple.nextTempVar();
		symbolTable.put(ins.result, new TempVarAttribute(ins.result, "boolean"));
		curMethodIR.addQuad(ins);
		return ins.result;
	}

	public String visit(Plus n) {
		Quadruple ins = new Quadruple(InstructionType.BINARY_ASSIGN);
		ins.operator = "+";
		ins.arg1 = n.e1.accept(this);
		ins.arg2 = n.e2.accept(this);
		ins.result = Quadruple.nextTempVar();
		symbolTable.put(ins.result, new TempVarAttribute(ins.result, "int"));
		curMethodIR.addQuad(ins);
		return ins.result;
	}

	public String visit(Minus n) {
		Quadruple ins = new Quadruple(InstructionType.BINARY_ASSIGN);
		ins.operator = "-";
		ins.arg1 = n.e1.accept(this);
		ins.arg2 = n.e2.accept(this);
		ins.result = Quadruple.nextTempVar();
		symbolTable.put(ins.result, new TempVarAttribute(ins.result, "int"));
		curMethodIR.addQuad(ins);
		return ins.result;
	}

	public String visit(Times n) {
		Quadruple ins = new Quadruple(InstructionType.BINARY_ASSIGN);
		ins.operator = "*";
		ins.arg1 = n.e1.accept(this);
		ins.arg2 = n.e2.accept(this);
		ins.result = Quadruple.nextTempVar();
		symbolTable.put(ins.result, new TempVarAttribute(ins.result, "int"));
		curMethodIR.addQuad(ins);
		return ins.result;
	}

	public String visit(ArrayLookup n) {
		Quadruple ins = new Quadruple(InstructionType.INDEXED_ASSIGN);
		ins.operator = "[]";
		ins.arg1 = n.e1.accept(this);
		ins.arg2 = n.e2.accept(this);
		ins.result = Quadruple.nextTempVar();
		symbolTable.put(ins.result, new TempVarAttribute(ins.result, "int"));
		curMethodIR.addQuad(ins);
		return ins.result;
	}

	public String visit(ArrayLength n) {
		Quadruple ins = new Quadruple(InstructionType.LENGTH);
		ins.operator = "length";
		ins.arg1 = n.e.accept(this);
		ins.result = Quadruple.nextTempVar();
		symbolTable.put(ins.result, new TempVarAttribute(ins.result, "int"));
		curMethodIR.addQuad(ins);
		return ins.result;
	}

	public String visit(Call n) {
  		Quadruple ins = new Quadruple(InstructionType.PARAM);
  		ins.operator = "param";
  		ins.arg1 = n.e.accept(this);
  		curMethodIR.addQuad(ins);

  		String exprType = getVarType(ins.arg1);

  		for (int i = 0; i < n.el.size(); i++) {
  			ins = new Quadruple(InstructionType.PARAM);
  			ins.operator = "param";
  			ins.arg1 = n.el.elementAt(i).accept(this);
  			curMethodIR.addQuad(ins);
  		}
  		int numParams = n.el.size() + 1;
		ins = new Quadruple(InstructionType.CALL);
		ins.operator = "call";
		// give arg1 the canonical method name
		ins.arg1 = exprType + "." + n.i.accept(this);
		ins.arg2 = ""+numParams;
		ins.result = Quadruple.nextTempVar();

		String resultType = getReturnType(ins.arg1);
		symbolTable.put(ins.result, new TempVarAttribute(ins.result, resultType));
		curMethodIR.addQuad(ins);
		return ins.result;
	}

	public String getReturnType(String canonicalMethod) {
		String className = canonicalMethod.split("\\.")[0];
		String methodName = canonicalMethod.split("\\.")[1];
		MethodAttribute method = classes.get(className).getMethod(methodName);
		return method.getReturnType();
	}

	public String getVarType(String identifier) {
		if (identifier.equals("this"))
			return curClass;

		Attribute var = (Attribute)symbolTable.get(identifier);
		if (var instanceof VariableAttribute) {
			return ((VariableAttribute)var).getType();
		} else if (var instanceof TempVarAttribute) {
			return ((TempVarAttribute)var).getType();
		}
		return "";
	}

	public String visit(IntegerLiteral n) {
		return ""+n.i;
	}

	public String visit(True n) {
		//return ""+(-1);
		return "true";
	}

	public String visit(False n) {
		//return ""+0;
		return "false";
	}

	public String visit(IdentifierExp n) {
		return n.s;
	}

	public String visit(This n) {
		return "this";
	}

	public String visit(NewArray n) {
		Quadruple ins = new Quadruple(InstructionType.NEW_ARRAY);
		ins.operator = "new";
		ins.arg1 = "int";
		ins.arg2 = n.e.accept(this);
		ins.result = Quadruple.nextTempVar();
		symbolTable.put(ins.result, new TempVarAttribute(ins.result, "int array"));
		curMethodIR.addQuad(ins);
		return ins.result;
	}

	public String visit(NewObject n) {
		Quadruple ins = new Quadruple(InstructionType.NEW);
		ins.operator = "new";
		ins.arg1 = n.i.accept(this);
		ins.result = Quadruple.nextTempVar();
		symbolTable.put(ins.result, new TempVarAttribute(ins.result, ins.arg1));
		curMethodIR.addQuad(ins);
		return ins.result;
	}

	public String visit(Not n) {
		Quadruple ins = new Quadruple(InstructionType.UNARY_ASSIGN);
		ins.operator = "!";
		ins.arg1 = n.e.accept(this);
		ins.result = Quadruple.nextTempVar();
		symbolTable.put(ins.result, new TempVarAttribute(ins.result, "boolean"));
		curMethodIR.addQuad(ins);
		return ins.result;
	}

	public String visit(Identifier n) {
		return n.s;
	}
}