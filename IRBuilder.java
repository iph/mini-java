import java.util.*;
import syntaxtree.*;
import ir.*;

public class IRBuilder implements IRVisitor {
	private SymbolTable symbolTable;
	private ArrayList<Quadruple> irCode;
	private HashMap<String, Integer> irLabelLocs;

	public IRBuilder(SymbolTable symTable) {
		symbolTable = symTable;
		irCode = new ArrayList<Quadruple>();
		irLabelLocs = new HashMap<String, Integer>();
	}

	public void resolveLabels() {
		for (Quadruple instruction : irCode) {
			switch (instruction.getType()) {
			case JUMP:
			case CALL:
				Integer loc = irLabelLocs.get(instruction.arg1);
				if (loc != null)
					instruction.arg1 = ""+loc.intValue();
				break;
			case COND_JUMP:
				Integer loc = irLabelLocs.get(instruction.arg2);
				if (loc != null)
					instruction.arg2 = ""+loc.intValue();
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

		for (int i = start; i <= end; i++) {
			System.out.println(irCode.get(i).toString());
		}
	}

	public void visit(Program n);
	public void visit(MainClass n);
	public void visit(ClassDeclSimple n);
	public void visit(ClassDeclExtends n);
	public void visit(VarDecl n);
	public void visit(MethodDecl n) {
		int start = irCode.size();
		// map the name of the method to its first instruction
		mapLabelLocation(n.i.s, start);
		// convert each statement into IR
		n.sl.accept(this);
		// add IR for return statement
		Quadruple ins = new Quadruple(RETURN);
		ins.result = n.e.accept(this);
		irCode.add(ins);

  		int end = irCode.size()-1;
  		printMethod(start, end);
	}
	public void visit(Formal n);
	public void visit(IntArrayType n);
	public void visit(BooleanType n);
	public void visit(IntegerType n);
	public void visit(IdentifierType n);
	public void visit(Block n);
	public void visit(If n);
	public void visit(While n);
	public void visit(Print n);
	public void visit(Assign n);
	public void visit(ArrayAssign n);
	// FIXME: the code below makes me die inside.
	public String visit(And n) {
		Quadruple ins = new Quadruple(BINARY_ASSIGN);
		ins.operator = "&&";
		ins.arg1 = n.e1.accept(this);
		ins.arg2 = n.e2.accept(this);
		ins.result = Quadruple.nextTempVar();
		irCode.add(ins);
		return ins.result;
	}
	public String visit(LessThan n) {
		Quadruple ins = new Quadruple(BINARY_ASSIGN);
		ins.operator = "<";
		ins.arg1 = n.e1.accept(this);
		ins.arg2 = n.e2.accept(this);
		ins.result = Quadruple.nextTempVar();
		irCode.add(ins);
		return ins.result;
	}
	public String visit(Plus n) {
		Quadruple ins = new Quadruple(BINARY_ASSIGN);
		ins.operator = "+";
		ins.arg1 = n.e1.accept(this);
		ins.arg2 = n.e2.accept(this);
		ins.result = Quadruple.nextTempVar();
		irCode.add(ins);
		return ins.result;
	}
	public String visit(Minus n) {
		Quadruple ins = new Quadruple(BINARY_ASSIGN);
		ins.operator = "-";
		ins.arg1 = n.e1.accept(this);
		ins.arg2 = n.e2.accept(this);
		ins.result = Quadruple.nextTempVar();
		irCode.add(ins);
		return ins.result;
	}
	public String visit(Times n) {
		Quadruple ins = new Quadruple(BINARY_ASSIGN);
		ins.operator = "*";
		ins.arg1 = n.e1.accept(this);
		ins.arg2 = n.e2.accept(this);
		ins.result = Quadruple.nextTempVar();
		irCode.add(ins);
		return ins.result;
	}
	public String visit(ArrayLookup n) {
		Quadruple ins = new Quadruple(INDEXED_ASSIGN);
		ins.arg1 = n.e1.accept(this);
		ins.arg2 = n.e2.accept(this);
		ins.result = Quadruple.nextTemp();
		irCode.add(ins);
		return ins.result;
	}
	public String visit(ArrayLength n) {
		// TODO: should array length itself have a result?
		Quadruple ins = new Quadruple(LENGTH);
		ins.arg1 = n.e.accept(this);
		ins.result = Quadruple.nextTemp();
		irCode.add(ins);
		return ins.result;
	}
	public String visit(Call n) {
  		// TODO: should calls themselves have results?
  		Quadruple ins = new Quadruple(PARAM);
  		ins.arg1 = n.e.accept(this);
  		irCode.add(ins);
  		for (int i = 0; i < n.el.size(); i++) {
  			ins = new Quadruple(PARAM);
  			ins.arg1 = n.el.elementAt(i).accept(this);
  			irCode.add(ins);
  		}
  		int numParams = n.el.size() + 1;
		ins = new Quadruple(CALL);
		ins.arg1 = n.i.accept(this);
		ins.arg2 = ""+numParams;
		ins.result = Quadruple.nextTemp();
		irCode.add(ins);
		return ins.result;
	}
	public String visit(IntegerLiteral n) {
		return ""+n.i;
	}
	public String visit(True n) {
		return ""+(-1);
	}
	public String visit(False n) {
		return ""+0;
	}
	public String visit(IdentifierExp n);
	public String visit(This n);
	public String visit(NewArray n);
	public String visit(NewObject n);
	public String visit(Not n);
	// TODO: for Identifier, it should be a pointer to
	//       symbol table entry
	public void visit(Identifier n);
}