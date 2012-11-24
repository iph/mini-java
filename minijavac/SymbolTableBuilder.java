package minijavac;

import java.util.*;
import minijavac.tools.MJToken;
import minijavac.syntaxtree.*;
import minijavac.visitor.*;

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
        addPrintlnMethod();
	}

	public void addPrintlnMethod() {
		MethodAttribute method = new MethodAttribute("System.out.println", -1, -1, "");
		symbolTable.put("System.out.println", method);
		symbolTable.startScope();
		VariableAttribute var = new VariableAttribute("out", -1, -1, "int");
		method.addParameter("out", var);
        symbolTable.put("out", var);
        symbolTable.endScope();
	}

	public SymbolTable getSymbolTable() {
		return symbolTable;
	}

	public void errorRedefinition(String id, int line, int column){
			System.out.printf("Multiply defined identifier %s at line %d, character %d\n", id, line, column);
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
        if(symbolTable.get(n.i1.s) != null)
		    errorRedefinition(n.i1.s, curLocation.line, curLocation.column);
		ClassAttribute cls = new ClassAttribute(n.i1.s, curLocation.line, curLocation.column);
		symbolTable.put(n.i1.s, cls);
		symbolTable.startScope();

		// TODO: Do we include args identifier? There is no method...damnit!
		// THERE ARE NO STRINGS! WHAT TYPE ARE YOU?
		// Edit (Joel): I don't think we should add the formal to the method attribute, since
		//              we'll never have the need to check it. But I do need a method attribute for my IR builder
		MethodAttribute method = new MethodAttribute("main", curLocation.line+1, curLocation.column+20, "");
		symbolTable.put("main", method);
		symbolTable.startScope();
		symbolTable.endScope();
		symbols.push(method);

		cls.addMethod("main", method);
		symbolTable.endScope();
	}
	/**
	* The basics for class
	*/
	public void visit(ClassDeclSimple n) {
		MJToken curLocation = location.get(n.i);

        if(symbolTable.get(n.i.s) != null)
            errorRedefinition(n.i.s, curLocation.line, curLocation.column);
		ClassAttribute cls = new ClassAttribute(n.i.s, curLocation.line, curLocation.column);
		symbolTable.put(n.i.s, cls);
        symbolTable.startScope();

        // Add all class variable definitions.
		for(int i = 0; i < n.vl.size(); i++){
			VarDecl v = n.vl.elementAt(i);
            MJToken token = location.get(v.i);
            if(cls.hasVariable(v.i.s)){
                errorRedefinition(v.i.s, token.line, token.column);
            }
			v.accept(this);
			VariableAttribute variable = (VariableAttribute) symbols.pop();
			cls.addVariable(v.i.s, variable);
		}

        // Add all method declarations, and check their scope shit as well.
        for(int i = 0; i < n.ml.size(); i++){
            MethodDecl m = n.ml.elementAt(i);
            MJToken token = location.get(m.i);
            if(cls.hasMethod(m.i.s)){
                errorRedefinition(m.i.s, token.line, token.column);
            }
            m.accept(this);
            MethodAttribute method = (MethodAttribute) symbols.pop();
            cls.addMethod(m.i.s, method);
        }
        symbolTable.endScope();
	}
	public void visit(ClassDeclExtends n) {

	}
	public void visit(VarDecl n){
        MJToken curLocation = location.get(n.i);
        String type = getType(n.t);
        VariableAttribute variable = new VariableAttribute(n.i.s, curLocation.line, curLocation.column, type);
        symbols.push(variable);
        symbolTable.put(n.i.s, variable);
	}

	public void visit(MethodDecl n) {
        MJToken curLocation = location.get(n.i);
        String retType = getType(n.t);
        MethodAttribute method = new MethodAttribute(n.i.s, curLocation.line, curLocation.column, retType);
        symbolTable.put(n.i.s, method);
        symbolTable.startScope();

        // Adding all the parameters to the meta data.
        for(int i = 0; i < n.fl.size(); i++){
            n.fl.elementAt(i).accept(this);
            VariableAttribute var = (VariableAttribute) symbols.pop();
            String varName = n.fl.elementAt(i).i.s;
            if(method.hasParameter(varName)){
                errorRedefinition(varName, var.getLine(), var.getColumn());
            }
            method.addParameter(varName,  var);
            symbolTable.put(varName, var);
        }

        //Adding all le variables in the method.
        for(int i = 0; i < n.vl.size(); i++){
            String varName = n.vl.elementAt(i).i.s;
            MJToken token = location.get(n.vl.elementAt(i).i);
             if(method.hasVariable(varName)){
                errorRedefinition(varName, token.line, token.column);
            }
            n.vl.elementAt(i).accept(this);
            VariableAttribute var = (VariableAttribute) symbols.pop();
            method.addVariable(varName, var);
        }
        symbolTable.endScope();
        symbols.push(method);
	}
	public void visit(Formal n){
        MJToken token = location.get(n.i);
        VariableAttribute variable = new VariableAttribute(n.i.s, token.line, token.column, getType(n.t));
        symbols.push(variable);
	}



	// Shit we don't need to do!
	public void visit(IntArrayType n){}
	public void visit(BooleanType n){}
	public void visit(IntegerType n){}
	public void visit(IdentifierType n){}
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
