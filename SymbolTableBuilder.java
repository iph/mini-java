import java.util.*;
import tools.MJToken;
import syntaxtree.*;
import visitor.*;

public class SymbolTableBuilder implements Visitor {
	SymbolTable symbolTable;
	Stack <Object> symbols;
	HashMap<Object, MJToken> location;
    boolean hasError;

	public SymbolTableBuilder(HashMap<Object, MJToken> l) {
		symbolTable = new SymbolTable();
		symbols = new Stack<Object>();
		location = l;
        hasError = false;
	}

	public SymbolTable getSymbolTable() {
		return symbolTable;
	}

	public void checkRedefinition(String id, MJToken token){

		if(symbolTable.hasId(id)){
			System.out.printf("Multiply defined identifier %s at line %d, character %d", id, token.line, token.column);
            hasError = true;
		}
	}

    public String getType(Type t){
        String type;
        if(t instanceof IntArrayType){
            type = "int array";
        }
        else if(t instanceof IntegerType){
            type = "int";
        }
        else if(t instanceof BooleanType){
            type = "boolean";
        }
        else{
            IdentifierType id= (IdentifierType)t;
            type = id.s;
        }
        return type;
    }
	// Shit we do need to do.
	public void visit(Program n){
        symbolTable.startScope();
		n.m.accept(this);
		for(int i = 0; i < n.cl.size(); i++){
			n.cl.elementAt(i).accept(this);
		}
	}
	public void visit(MainClass n){
		MJToken curLocation = location.get(n.i1);
		checkRedefinition(n.i1.s, curLocation);
		ClassAttribute cls = new ClassAttribute(curLocation.line, curLocation.column);
		symbolTable.put(n.i1.s, cls);

		//TODO: Do we include args identifier? There is no method...damnit!
		// THERE ARE NO STRINGS! WHAT TYPE ARE YOU?
	}
	/**
	* The basics for class
	*/
	public void visit(ClassDeclSimple n) {
		MJToken curLocation = location.get(n.i);

		checkRedefinition(n.i.s, curLocation);
		ClassAttribute cls = new ClassAttribute(curLocation.line, curLocation.column);
		symbolTable.put(n.i.s, cls);
        symbolTable.startScope();

        // Add all class variable definitions.
		for(int i = 0; i < n.vl.size(); i++){
			VarDecl v = n.vl.elementAt(i);
            checkRedefinition(v.i.s, location.get(v.i));
			v.accept(this);
			VariableAttribute variable = (VariableAttribute) symbols.pop();
			cls.addVariable(v.i.s, variable);
            symbolTable.put(v.i.s, variable);

		}

        // Add all method declarations, and check their scope shit as well.
        for(int i = 0; i < n.ml.size(); i++){
            MethodDecl m = n.ml.elementAt(i);
            checkRedefinition(m.i.s, location.get(m.i));
            m.accept(this);
            MethodAttribute method = (MethodAttribute) symbols.pop();
            cls.addMethod(m.i.s, method);
            symbolTable.put(m.i.s, method);

        }
        symbolTable.endScope();
        System.out.println(symbolTable);
	}
	public void visit(ClassDeclExtends n) {

	}
	public void visit(VarDecl n){
        MJToken curLocation = location.get(n.i);
        String type = getType(n.t);
        VariableAttribute variable = new VariableAttribute(curLocation.line, curLocation.column, type);
        symbols.push(variable);
	}
	public void visit(MethodDecl n) {
        MJToken curLocation = location.get(n.i);
        String retType = getType(n.t);
        MethodAttribute method = new MethodAttribute(curLocation.line, curLocation.column, retType);

	}
	public void visit(Formal n){

	}
	public void visit(IntArrayType n){

	}
	public void visit(BooleanType n){

	}
	public void visit(IntegerType n){

	}
	public void visit(IdentifierType n){

	}





	// Shit we don't need to do!
	public void visit(Block n){}
	public void visit(If n){}
	public void visit(While n){}
	public void visit(Assign n){}
	public void visit(ArrayAssign n){}
	public void visit(And n){}
	public void visit(LessThan n){}
	public void visit(Plus n){}
	public void visit(Minus n){}
	public void visit(Times n){}
	public void visit(ArrayLookup n){}
	public void visit(ArrayLength n){}
	public void visit(Call n){}
	public void visit(IntegerLiteral n){}
	public void visit(True n){}
	public void visit(False n){}
	public void visit(IdentifierExp n){}
	public void visit(This n){}
	public void visit(Print n){}
	public void visit(Identifier n){}
	public void visit(Not n){}
	public void visit(NewArray n){}
	public void visit(NewObject n){}

}
