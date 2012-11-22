import java.util.*;
import syntaxtree.*;
import ir.*;
import visitor.IRVisitor;

// TODO: backpatching instead of what is done currently
//       short-circuiting?
//       True/False representation?

public class IRBuilder implements IRVisitor {
	//private SymbolTable symbolTable;
	private ArrayList<Quadruple> irCode;
	private HashMap<String, Integer> irLabelLocs;

	public IRBuilder() {
		//symbolTable = symTable;
		irCode = new ArrayList<Quadruple>();
		irLabelLocs = new HashMap<String, Integer>();
	}

	public void resolveLabels() {
		for (Quadruple instruction : irCode) {
			switch (instruction.getType()) {
			case JUMP:
			case COND_JUMP:
			case CALL:
				Integer loc = irLabelLocs.get(instruction.arg1);
				if (loc != null)
					instruction.arg1 = ""+loc.intValue();
				break;
			}
		}
	}

	private void mapLabelLocation(String label, int location) {
		irLabelLocs.put(label, new Integer(location));
	}

	private void printMethod(int start, int end) {
		if (end < start)
			return;

		System.out.println();
		for (int i = start; i <= end; i++) {
			System.out.println(i + ": " + irCode.get(i).toString());
		}
	}

	public void visit(Program n) {
	    n.m.accept(this);
	    for (int i = 0; i < n.cl.size(); i++) {
	        n.cl.elementAt(i).accept(this);
	    }
	}
	public void visit(MainClass n) {
		int start = irCode.size();
		// map the name of the method to its first instruction
		mapLabelLocation(n.i2.s, start);
		// convert statement to IR
    	n.s.accept(this);

  		int end = irCode.size() - 1;
  		printMethod(start, end);
	}
	public void visit(ClassDeclSimple n) {
  		for (int i = 0; i < n.ml.size(); i++) {
  			n.ml.elementAt(i).accept(this);
  		}
	}
	public void visit(ClassDeclExtends n) {
  		for (int i = 0; i < n.ml.size(); i++) {
  			n.ml.elementAt(i).accept(this);
  		}
	}
	public void visit(VarDecl n) {}
	public void visit(MethodDecl n) {
		int start = irCode.size();
		// map the name of the method to its first instruction
		mapLabelLocation(n.i.s, start);
		// convert each statement into IR
  		for (int i = 0; i < n.sl.size(); i++) {
  			n.sl.elementAt(i).accept(this);
  		}
		// add IR for return statement
		Quadruple ins = new Quadruple(InstructionType.RETURN);
		ins.operator = "return";
		ins.arg1 = n.e.accept(this);
		irCode.add(ins);

  		int end = irCode.size() - 1;
  		printMethod(start, end);
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
		Quadruple ins = new Quadruple(InstructionType.COND_JUMP);
		ins.operator = "iftrue";
		ins.arg2 = n.e.accept(this);
		irCode.add(ins);
		int blankTrue = irCode.size() - 1;
		// generate else code
		n.s2.accept(this);
		ins = new Quadruple(InstructionType.JUMP);
		ins.operator = "goto";
		irCode.add(ins);
		int blankFalse = irCode.size() - 1;
		int l1 = blankFalse + 1;
		// generate if code
		n.s1.accept(this);
		int l2 = irCode.size();
		irCode.get(blankTrue).arg1 = ""+l1;
		irCode.get(blankFalse).arg1 = ""+l2;
	}
	public void visit(While n) {
		// L1 is the beginning of our loop
		int l1 = irCode.size();
		Quadruple ins = new Quadruple(InstructionType.COND_JUMP);
		ins.operator = "iftrue";
		ins.arg2 = n.e.accept(this);
		// L2 comes after the expression code + the iftrue + the goto
		int l2 = irCode.size() + 2;
		ins.arg1 = ""+l2;
		irCode.add(ins);
		ins = new Quadruple(InstructionType.JUMP);
		ins.operator = "goto";
		// leave arg1 blank for now since we don't know where L3 is yet
		irCode.add(ins);
		int blankCode = l2-1;
		// generate statement code
		n.s.accept(this);
		ins = new Quadruple(InstructionType.JUMP);
		ins.operator = "goto";
		ins.arg1 = ""+l1;
		irCode.add(ins);
		// now we know where L3 is, so update the second goto
		int l3 = irCode.size();
		irCode.get(blankCode).arg1 = ""+l3;
	}
	public void visit(Print n) {
		Quadruple ins = new Quadruple(InstructionType.PARAM);
		ins.operator = "param";
		ins.arg1 = n.e.accept(this);
		irCode.add(ins);
		ins = new Quadruple(InstructionType.CALL);
		ins.operator = "call";
		ins.arg1 = "System.out.println";
		// System.out.println takes 1 param (an int)
		ins.arg2 = ""+1;
		// TODO: should we pretend like it has a result, and ignore it?
		irCode.add(ins);
	}
	public void visit(Assign n) {
		Quadruple ins = new Quadruple(InstructionType.COPY);
		ins.operator = ":=";
		ins.result = n.i.accept(this);
		ins.arg1 = n.e.accept(this);
		irCode.add(ins);
	}
	public void visit(ArrayAssign n) {
		Quadruple ins = new Quadruple(InstructionType.ARRAY_ASSIGN);
		ins.operator = "[]";
		ins.result = n.i.accept(this);
		ins.arg1 = n.e1.accept(this);
		ins.arg2 = n.e2.accept(this);
		irCode.add(ins);
	}
	public String visit(And n) {
		Quadruple ins = new Quadruple(InstructionType.BINARY_ASSIGN);
		ins.operator = "&&";
		ins.arg1 = n.e1.accept(this);
		ins.arg2 = n.e2.accept(this);
		ins.result = Quadruple.nextTempVar();
		irCode.add(ins);
		return ins.result;
	}
	public String visit(LessThan n) {
		Quadruple ins = new Quadruple(InstructionType.BINARY_ASSIGN);
		ins.operator = "<";
		ins.arg1 = n.e1.accept(this);
		ins.arg2 = n.e2.accept(this);
		ins.result = Quadruple.nextTempVar();
		irCode.add(ins);
		return ins.result;
	}
	public String visit(Plus n) {
		Quadruple ins = new Quadruple(InstructionType.BINARY_ASSIGN);
		ins.operator = "+";
		ins.arg1 = n.e1.accept(this);
		ins.arg2 = n.e2.accept(this);
		ins.result = Quadruple.nextTempVar();
		irCode.add(ins);
		return ins.result;
	}
	public String visit(Minus n) {
		Quadruple ins = new Quadruple(InstructionType.BINARY_ASSIGN);
		ins.operator = "-";
		ins.arg1 = n.e1.accept(this);
		ins.arg2 = n.e2.accept(this);
		ins.result = Quadruple.nextTempVar();
		irCode.add(ins);
		return ins.result;
	}
	public String visit(Times n) {
		Quadruple ins = new Quadruple(InstructionType.BINARY_ASSIGN);
		ins.operator = "*";
		ins.arg1 = n.e1.accept(this);
		ins.arg2 = n.e2.accept(this);
		ins.result = Quadruple.nextTempVar();
		irCode.add(ins);
		return ins.result;
	}
	public String visit(ArrayLookup n) {
		Quadruple ins = new Quadruple(InstructionType.INDEXED_ASSIGN);
		ins.operator = "[]";
		ins.arg1 = n.e1.accept(this);
		ins.arg2 = n.e2.accept(this);
		ins.result = Quadruple.nextTempVar();
		irCode.add(ins);
		return ins.result;
	}
	public String visit(ArrayLength n) {
		Quadruple ins = new Quadruple(InstructionType.LENGTH);
		ins.operator = "length";
		ins.arg1 = n.e.accept(this);
		ins.result = Quadruple.nextTempVar();
		irCode.add(ins);
		return ins.result;
	}
	public String visit(Call n) {
  		Quadruple ins = new Quadruple(InstructionType.PARAM);
  		ins.operator = "param";
  		ins.arg1 = n.e.accept(this);
  		irCode.add(ins);
  		for (int i = 0; i < n.el.size(); i++) {
  			ins = new Quadruple(InstructionType.PARAM);
  			ins.operator = "param";
  			ins.arg1 = n.el.elementAt(i).accept(this);
  			irCode.add(ins);
  		}
  		int numParams = n.el.size() + 1;
		ins = new Quadruple(InstructionType.CALL);
		ins.operator = "call";
		ins.arg1 = n.i.accept(this);
		ins.arg2 = ""+numParams;
		ins.result = Quadruple.nextTempVar();
		irCode.add(ins);
		return ins.result;
	}
	public String visit(IntegerLiteral n) {
		return ""+n.i;
	}
	public String visit(True n) {
		//return ""+(-1);
		return "True";
	}
	public String visit(False n) {
		//return ""+0;
		return "False";
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
		irCode.add(ins);
		return ins.result;
	}
	public String visit(NewObject n) {
		Quadruple ins = new Quadruple(InstructionType.NEW);
		ins.operator = "new";
		ins.arg1 = n.i.accept(this);
		ins.result = Quadruple.nextTempVar();
		irCode.add(ins);
		return ins.result;
	}
	public String visit(Not n) {
		Quadruple ins = new Quadruple(InstructionType.UNARY_ASSIGN);
		ins.operator = "!";
		ins.arg1 = n.e.accept(this);
		ins.result = Quadruple.nextTempVar();
		irCode.add(ins);
		return ins.result;
	}
	// TODO: for Identifier, it should be a pointer to
	//       symbol table entry
	public String visit(Identifier n) {
		return n.s;
	}
}