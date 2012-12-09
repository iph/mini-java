package minijavac;

import java.util.*;
import minijavac.syntaxtree.*;
import minijavac.ir.*;
import minijavac.visitor.IRVisitor;

public class IRBuilder implements IRVisitor {
	private SymbolTable symbolTable;
	private HashMap<String, ClassAttribute> classes;
	private IR ir;
	// FIXME: shouldn't need to keep track of this
	private String curClass;
	private MethodAttribute curMethod;
	private MethodIR curMethodIR;

	public IRBuilder(SymbolTable symTable) {
		symbolTable = symTable;
		storeClasses();
		ir = new IR();
		curMethodIR = null;
		curClass = "";
	}

	public IR getIR() {
		return ir;
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

	private void addTemp(String identifier, String type) {
		curMethod.addVariable(identifier, new VariableAttribute(identifier, type));
		curMethod.getInMyScope(symbolTable);
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
		}
		return "";
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
		
		curMethod = method;

		// use fully-qualified method name for IR
		MethodIR methodIR = new MethodIR(className, "main", symbolTable);
		curMethodIR = methodIR;
		// convert statement to IR
    	n.s.accept(this);

    	ir.addMethodIR(methodIR);
    	
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

        // TODO: Should the method IR of the parent class be printed out?
        //       Should I re-visit the AST nodes of the parent class here and re-generate IR?
        //         That seems kinda silly and problematic ('this' would resolve to this class, not the parent)
        //       Should I create labels corresponding to the inherited method names that are just
        //         gotos to the parent class's method?
        //       Should anything be done at all?
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
        
        curMethod = method;
        
        // use fully-qualified method name for IR
		MethodIR methodIR = new MethodIR(curClass, n.i.s, symbolTable);
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

		ir.addMethodIR(methodIR);

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
		String l1 = curMethodIR.nextLabel();
		ins.arg1 = l1;
		ins.arg2 = expression;
		curMethodIR.addQuad(ins);

		// generate else code
		n.s2.accept(this);

		// jump from end of else to endif
		ins = new Quadruple(InstructionType.JUMP);
		ins.operator = "goto";
		String l2 = curMethodIR.nextLabel();
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
		String l1 = curMethodIR.nextLabel();
		// l1 should resolve to the first quad of the expression
		curMethodIR.addFutureLabel(l1, curMethodIR.size());
		String expression = n.e.accept(this);

		Quadruple ins = new Quadruple(InstructionType.COND_JUMP);
		ins.operator = "iftrue";
		String l2 = curMethodIR.nextLabel();
		ins.arg1 = l2;
		ins.arg2 = expression;
		curMethodIR.addQuad(ins);

		// jump to end of while
		ins = new Quadruple(InstructionType.JUMP);
		ins.operator = "goto";
		String l3 = curMethodIR.nextLabel();
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
		ins.result = curMethodIR.nextTempVar();
		addTemp(ins.result, "boolean");
		curMethodIR.addQuad(ins);
		return ins.result;
	}

	public String visit(LessThan n) {
		Quadruple ins = new Quadruple(InstructionType.BINARY_ASSIGN);
		ins.operator = "<";
		ins.arg1 = n.e1.accept(this);
		ins.arg2 = n.e2.accept(this);
		ins.result = curMethodIR.nextTempVar();
		addTemp(ins.result, "boolean");
		curMethodIR.addQuad(ins);
		return ins.result;
	}

	public String visit(Plus n) {
		Quadruple ins = new Quadruple(InstructionType.BINARY_ASSIGN);
		ins.operator = "+";
		ins.arg1 = n.e1.accept(this);
		ins.arg2 = n.e2.accept(this);
		ins.result = curMethodIR.nextTempVar();
		addTemp(ins.result, "int");
		curMethodIR.addQuad(ins);
		return ins.result;
	}

	public String visit(Minus n) {
		Quadruple ins = new Quadruple(InstructionType.BINARY_ASSIGN);
		ins.operator = "-";
		ins.arg1 = n.e1.accept(this);
		ins.arg2 = n.e2.accept(this);
		ins.result = curMethodIR.nextTempVar();
		addTemp(ins.result, "int");
		curMethodIR.addQuad(ins);
		return ins.result;
	}

	public String visit(Times n) {
		Quadruple ins = new Quadruple(InstructionType.BINARY_ASSIGN);
		ins.operator = "*";
		ins.arg1 = n.e1.accept(this);
		ins.arg2 = n.e2.accept(this);
		ins.result = curMethodIR.nextTempVar();
		addTemp(ins.result, "int");
		curMethodIR.addQuad(ins);
		return ins.result;
	}

	public String visit(ArrayLookup n) {
		Quadruple ins = new Quadruple(InstructionType.INDEXED_ASSIGN);
		ins.operator = "[]";
		ins.arg1 = n.e1.accept(this);
		ins.arg2 = n.e2.accept(this);
		ins.result = curMethodIR.nextTempVar();
		addTemp(ins.result, "int");
		curMethodIR.addQuad(ins);
		return ins.result;
	}

	public String visit(ArrayLength n) {
		Quadruple ins = new Quadruple(InstructionType.LENGTH);
		ins.operator = "length";
		ins.arg1 = n.e.accept(this);
		ins.result = curMethodIR.nextTempVar();
		addTemp(ins.result, "int");
		curMethodIR.addQuad(ins);
		return ins.result;
	}

	public String visit(Call n) {
  		ArrayList<String> params = new ArrayList<String>();
  		// the first param is the object itself
  		params.add(n.e.accept(this));
  		// add the rest of the params to the list
  		for (int i = 0; i < n.el.size(); i++) {
  			params.add(n.el.elementAt(i).accept(this));
  		}

  		String exprType = getVarType(params.get(0));

  		Quadruple ins;
  		for (int i = 0; i < params.size(); i++) {
  			ins = new Quadruple(InstructionType.PARAM);
  			ins.operator = "param";
  			ins.arg1 = params.get(i);
  			curMethodIR.addQuad(ins);
  		}
  		int numParams = n.el.size() + 1;
		ins = new Quadruple(InstructionType.CALL);
		ins.operator = "call";
		// find which class has the method we're calling
		String methodName = n.i.accept(this);
		ClassAttribute klass = classes.get(exprType);
		klass = klass.getClassDefiningMethod(methodName);
		// give arg1 the canonical method name
		ins.arg1 = klass.getIdentifier() + "." + methodName;
		ins.arg2 = ""+numParams;
		ins.result = curMethodIR.nextTempVar();

		String resultType = getReturnType(ins.arg1);
		addTemp(ins.result, resultType);
		curMethodIR.addQuad(ins);
		return ins.result;
	}

	public String visit(IntegerLiteral n) {
		Quadruple ins = new Quadruple(InstructionType.COPY);
		ins.operator = ":=";
		ins.arg1 = ""+n.i;
		ins.result = curMethodIR.nextTempVar();
		addTemp(ins.result, "int");
		curMethodIR.addQuad(ins);
		return ins.result;
	}

	public String visit(True n) {
		Quadruple ins = new Quadruple(InstructionType.COPY);
		ins.operator = ":=";
		ins.arg1 = "true";
		ins.result = curMethodIR.nextTempVar();
		addTemp(ins.result, "boolean");
		curMethodIR.addQuad(ins);
		return ins.result;
	}

	public String visit(False n) {
		Quadruple ins = new Quadruple(InstructionType.COPY);
		ins.operator = ":=";
		ins.arg1 = "false";
		ins.result = curMethodIR.nextTempVar();
		addTemp(ins.result, "boolean");
		curMethodIR.addQuad(ins);
		return ins.result;
	}

	public String visit(IdentifierExp n) {
		Quadruple ins = new Quadruple(InstructionType.COPY);
		ins.operator = ":=";
		ins.arg1 = n.s;
		ins.result = curMethodIR.nextTempVar();
		addTemp(ins.result, getVarType(n.s));
		curMethodIR.addQuad(ins);
		return ins.result;
	}

	public String visit(This n) {
		Quadruple ins = new Quadruple(InstructionType.COPY);
		ins.operator = ":=";
		ins.arg1 = "this";
		ins.result = curMethodIR.nextTempVar();
		addTemp(ins.result, getVarType("this"));
		curMethodIR.addQuad(ins);
		return ins.result;
	}

	public String visit(NewArray n) {
		Quadruple ins = new Quadruple(InstructionType.NEW_ARRAY);
		ins.operator = "new";
		ins.arg1 = "int";
		ins.arg2 = n.e.accept(this);
		ins.result = curMethodIR.nextTempVar();
		addTemp(ins.result, "int array");
		curMethodIR.addQuad(ins);
		return ins.result;
	}

	public String visit(NewObject n) {
		Quadruple ins = new Quadruple(InstructionType.NEW);
		ins.operator = "new";
		ins.arg1 = n.i.accept(this);
		ins.result = curMethodIR.nextTempVar();
		addTemp(ins.result, ins.arg1);
		curMethodIR.addQuad(ins);
		return ins.result;
	}

	public String visit(Not n) {
		Quadruple ins = new Quadruple(InstructionType.UNARY_ASSIGN);
		ins.operator = "!";
		ins.arg1 = n.e.accept(this);
		ins.result = curMethodIR.nextTempVar();
		addTemp(ins.result, "boolean");
		curMethodIR.addQuad(ins);
		return ins.result;
	}

	public String visit(Identifier n) {
		return n.s;
	}
}
