package minijavac;

import minijavac.syntaxtree.*;
import minijavac.visitor.*;
import java.util.*;
import minijavac.tools.MJToken;

public class SemanticChecker implements SemanticVisitor {
    private SymbolTable environment;
    private HashMap<Object, MJToken> location; //Still need this!
    //A dumb way to figure out when a statement is in main.
    private boolean inMain, hasError;
    private String curClass;

    public SemanticChecker(SymbolTable s, HashMap<Object, MJToken> l){
        location = l;
        environment = s;
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

    public void visit(Program n){
        n.m.accept(this);
        for(int i = 0; i < n.cl.size(); i++){
            n.cl.elementAt(i).accept(this);
        }
    }

    public void visit(MainClass n){
        MJToken token = location.get(n.i2);
        environment.startScope();
        curClass = n.i1.s;
        inMain = true;
        //Only time we need to manually input stuff, hopefully.
        VariableAttribute strArg = new VariableAttribute(n.i2.s, token.line, token.column, "String array");
        inMain = false;
        environment.put(n.i2.s, strArg);

        n.s.accept(this);
        environment.endScope();
    }

    public void visit(ClassDeclSimple n){
        //Setup
        environment.startScope();
        ClassAttribute cls = (ClassAttribute)environment.get(n.i.s);
        cls.getInMyScope(environment);
        curClass = n.i.s;
        //TRAVERSAL!!!
        for(int i = 0; i < n.ml.size(); i++){
            n.ml.elementAt(i).accept(this);
        }

        //Teardown
        environment.endScope();
    }
    public void visit(ClassDeclExtends n){
        //TODO: STILL NEED TO FIGURE OUT EXTENDS
        curClass = n.i.s;
    }

    public void visit(MethodDecl n){
        //Setup
        environment.startScope();
        MethodAttribute meth = (MethodAttribute)environment.get(n.i.s);
        meth.getInMyScope(environment);

        //TIME TO GO THROUGH REAL STATEMENTS!
        for(int i = 0; i < n.sl.size(); i++){
            n.sl.elementAt(i).accept(this);
        }
        //Evaluate the return expression as well..
        //TODO: Should we treat this differently since it is a return?
        // YES! Gotta check the type vs declared return type.
        // TODO: Does that mean we need to return something other than void? for visitor
        // YES, GONNA HAVE TO CHANGE THIS FUCKER SOON.
        n.e.accept(this);

        //Teardown
        environment.endScope();
    }

    //Expression work.
    // TODO: As explained above, these can't return void, but need to return
    //       a real type back in the form of a string.
    // Current types:
    //       "int"
    //       "int array"
    //       "String array" (it's capitalized because I CAN!)
    //       "boolean"
    //       "#{id_type}" the actual id for a class.
    //       Discuss this with me over text plox.
    //
    // TODO: Implementation work shouldn't be too bad. All the scope is currently
    //       defined, so use the environment.get(String) to get any attributes.
    //       To check an attribute, just use instance of.
    public String visit(Call n){
        //TODO: Check to see if the identifier is a method.
        if(!environment.hasId(n.i.s) || !(environment.get(n.i.s) instanceof MethodAttribute)){
            MJToken token = location.get(n);
            System.out.printf("Attempt to call a non-method at line %d, character %d\n",
                              token.line, token.column);
            hasError = true;
            return "";
        }
        MethodAttribute meth = (MethodAttribute)environment.get(n.i.s);
        //TODO: Check to see if num arguments match.
        if(meth.parameterListSize() != n.el.size()){
           MJToken token = location.get(n);
           System.out.printf("Call of method %s does not match its declared number of arguments at line %d, character %d\n",
                             n.i.s, token.line, token.column);
           hasError = true;
           return "";
        }
        //TODO: Check to see if the type of an argument doesn't match.
        return ""; //FIXME: temporary so javac doesn't bitch
    }
    public String visit(And n) {
        //TODO: Check boolean in expressions.
        if (!n.e1.accept(this).equals("boolean") ||
            !n.e2.accept(this).equals("boolean")) {
            MJToken token = location.get(n);
            System.out.printf("Attempt to use boolean operator && on non-boolean operands at line %d, character %d\n",
                              token.line, token.column);
            hasError = true;
            return "";
        }
        return "boolean";
    }
    public String visit(ArrayLookup n) {
        return "int";
    }
    public String visit(IntegerLiteral n) {
        return "int";
    }
    public String visit(True n) {
        return "boolean";
    }
    public String visit(False n) {
        return "boolean";
    }
    public String visit(IdentifierExp n) {
        //TODO: Check if the identifier is a method/class.
        Attribute attr = (Attribute)environment.get(n.s);
        if(!(attr instanceof VariableAttribute)){
            MJToken token = location.get(n);
            String type = "method";
            if (attr instanceof ClassAttribute) {
                type = "class";
            }
            System.out.printf("Invalid r-value: %s is a %s, at line %d, character %d\n",
                              n.s, type, token.line, token.column);
            hasError = true;
            return "";
        }
        return ((VariableAttribute)attr).getType();
    }
    public String visit(This n) {
        if(inMain){
            MJToken token = location.get(n);
            System.out.printf("Illegal use of keyword ‘this’ in static method at line %d, character %d\n",
                             token.line, token.column);
            hasError = true;
            return "";
        }
        return curClass;
    }
    public String visit(NewArray n){
        return "int array";
    }
    public String visit(NewObject n){
        return n.i.s;
    }
    public String visit(Not n){
        //TODO: Check boolean in expressions.
        return "boolean";
    }
    public String visit(ArrayLength n) {
        //TODO: Make sure int[] is used.
        if (!n.e.accept(this).equals("int array")) {
            MJToken token = location.get(n);
            System.out.printf("Length property only applies to arrays, line %d, character %d\n",
                             token.line, token.column);
            hasError = true;
            return "";
        }
        return "int";
    }
    public String visit(LessThan n) {
        //TODO: Check integer for expressions
        return "boolean";
    }
    public String visit(Plus n) {
        //TODO: Check integer for expressions.
        return "int";
    }
    public String visit(Minus n) {
        //TODO: Check integer for expresions.
        return "int";
    }
    public String visit(Times n) {
        //TODO: Check integer for expressions.
        return "int";
    }

    public String visit(Identifier n) {
        return n.s;
    }

    //Statement work.
    //TODO: Again, statement scope is already defined. It is a matter of
    //      implementing the checks on expression and individual statements.
    public void visit(Block n){}
    public void visit(If n){
        //TODO: Make sure boolean evaluation.
        if (!n.e.accept(this).equals("boolean")) {
            MJToken token = location.get(n);
            System.out.printf("Non-boolean expression used as the condition of if statement at line %d, character %d\n",
                              token.line, token.column);
            hasError = true;
            return;
        }
    }
    public void visit(While n){
        //TODO: Make sure boolean evaluation.
        if (!n.e.accept(this).equals("boolean")) {
            MJToken token = location.get(n);
            System.out.printf("Non-boolean expression used as the condition of while statement at line %d, character %d\n",
                              token.line, token.column);
            hasError = true;
            return;
        }
    }
    public void visit(Print n){}
    public void visit(Assign n){
        String exprType = n.e.accept(this);
        // FIXME: is this check necessary?
        if(!environment.hasId(n.i.s)){
            System.out.printf("WHAT YOU DOING STUPID?\n");
            hasError = true;
            return;
        }

        Attribute attr = (Attribute)environment.get(n.i.s);
        //Check for left value assignment of this or class/method name.
        if (n.i.s.equals("this") || !(attr instanceof VariableAttribute)) {
            MJToken token = location.get(n);
            String type = "reference"; // FIXME: what do we call a 'this'?
            if (attr instanceof ClassAttribute) {
                type = "class";
            } else if (attr instanceof MethodAttribute) {
                type = "method";
            }
            System.out.printf("Invalid l-value, %s is a %s, at line %d, character %d\n",
                              n.i.s, type, token.line, token.column);
            hasError = true;
            return;
        }

        //TODO: Make sure right hand type == left hand type.
        //TODO: deal with polymorphism?
        String identifierType = ((VariableAttribute)attr).getType();
        if (!exprType.equals(identifierType)) {
            MJToken token = location.get(n);
            System.out.printf("Type mismatch during assignment at line %d, character %d\n",
                              token.line, token.column);
            hasError = true;
            return;
        }
    }
    public void visit(ArrayAssign n){
        //String expressionInt = n.e1.accept(this);
        //String expressionInt2 = n.e2.accept(this);

        if(!environment.hasId(n.i.s)){
            System.out.printf("WHAT YOU DOING STUPID?\n");
            hasError = true;
            return;
        }
        //Check for left value assignment of this or class/method name.
        if(n.i.s.equalsIgnoreCase("this") || !(environment.get(n.i.s) instanceof VariableAttribute)){
            MJToken token = location.get(n);
            String type = "reference"; // FIXME: what do we call a 'this'?
            if (environment.get(n.i.s) instanceof ClassAttribute) {
                type = "class";
            } else if (environment.get(n.i.s) instanceof MethodAttribute) {
                type = "method";
            }
           System.out.printf("Invalid l-value, %s is a %s, at line %d, character %d\n",
                            n.i.s, type, token.line, token.column);
           hasError = true;
           return;
        }
    }

    //Uhh...No work? Maybe we can do types...
    public void visit(VarDecl n){}
    public String visit(IntArrayType n){
        return "int array";
    }
    public String visit(BooleanType n){
        return "boolean";
    }
    public String visit(IntegerType n){
        return "int";
    }
    public String visit(IdentifierType n){
        return n.s;
    }
    public String visit(Formal n){
        return getType(n.t);
    }
}
