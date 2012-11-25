package minijavac;

import minijavac.syntaxtree.*;
import minijavac.visitor.*;
import java.util.*;
import minijavac.tools.MJToken;

public class SemanticChecker implements SemanticVisitor {
    private SymbolTable environment;
    private HashMap<Object, MJToken> location; //Still need this!
    //A dumb way to figure out when a statement is in main.
    public boolean inMain, hasError;
    private String curClass;

    public SemanticChecker(SymbolTable s, HashMap<Object, MJToken> l){
        location = l;
        environment = s;
    }

    private String getType(Type t){
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

    public boolean isIdentifier(String type){
        return !type.equalsIgnoreCase("int") && !type.equalsIgnoreCase("int array") && !type.equalsIgnoreCase("boolean");
    }

    public boolean hasBadIdentifier(Exp e, String op){
        if(e instanceof IdentifierExp && environment.hasId(op) && !(environment.get(op) instanceof VariableAttribute)){
            return true;
        }
        return false;
    }
    private boolean isSubclassOf(String classNameA, String classNameB) {
        // FIXME: We need some kind of assurance that we're getting a
        //        ClassAttribute back, and not something else that happens
        //        to have the same name. Keep a hashmap of classes? Add
        //        methods to symbol table to do that?
        // FIX: Instanceof.
        Object classA = environment.get(classNameA);
        if (classA == null || !(classA instanceof ClassAttribute)) {
            // it's a primitive type
            return false;
        }
        return (((ClassAttribute)classA).hasSuperclass(classNameB));
    }

    public void visit(Program n){
        n.m.accept(this);
        for(int i = 0; i < n.cl.size(); i++){
            n.cl.elementAt(i).accept(this);
        }
    }

    public void visit(MainClass n){
        String className = n.i1.s;
        environment.startScope();
        ClassAttribute cls = (ClassAttribute)environment.get(className);
        cls.getInMyScope(environment);
        curClass = className;
        environment.startScope();
        MethodAttribute method = (MethodAttribute)environment.get("main");
        method.getInMyScope(environment);
        inMain = true;
        n.s.accept(this);
        environment.endScope();
        inMain = false;
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
        n.e.accept(this);

        //Teardown
        environment.endScope();
    }

    //Expression work.
       public String visit(Call n){
        String objType = n.e.accept(this);

        // Is this an object we're dealing with?
        if (!environment.hasId(objType) || !(environment.get(objType) instanceof ClassAttribute)) {
            MJToken token = location.get(n);
            System.out.printf("Attempt to call a non-method at line %d, character %d\n",
                              token.line, token.column);
            hasError = true;
            return "";
        }

        // Make sure the method exists on the object
        ClassAttribute objClass = (ClassAttribute)environment.get(objType);
        if (!objClass.hasMethod(n.i.s)) {
            MJToken token = location.get(n);
            System.out.printf("Attempt to call a non-method at line %d, character %d\n",
                              token.line, token.column);
            hasError = true;
            return "";
        }

        MethodAttribute meth = (MethodAttribute)objClass.getMethod(n.i.s);
        // Check to see if num arguments match.
        if(meth.parameterListSize() != n.el.size()){
           MJToken token = location.get(n);
           System.out.printf("Call of method %s does not match its declared number of arguments at line %d, character %d\n",
                             n.i.s, token.line, token.column);
           hasError = true;
           return meth.getReturnType();
        }

        // Check to see if the type of an argument doesn't match.
        for(int i = 0; i < n.el.size(); i++){
            String type = n.el.elementAt(i).accept(this);
            VariableAttribute var = meth.getParameter(i);
            if(!type.equals(var.getType())){
               MJToken token = location.get(n);
                System.out.printf("Call of method %s does not match its declared signature at line %d, character %d\n", n.i.s, token.line, token.column);
                hasError = true;
                break;
            }

        }

        return meth.getReturnType();
    }

    public String visit(And n) {
        // Check integer for expresions.
        String e1str = n.e1.accept(this);
        String e2str = n.e2.accept(this);
        if(hasBadIdentifier(n.e1, e1str)){
            MJToken token = location.get(n.e1);
            System.out.printf("Invalid operands for && operator, at line %d, character %d\n", token.line, token.column);
            hasError = true;
        }
        if(hasBadIdentifier(n.e2, e2str)){
            MJToken token = location.get(n.e2);
            System.out.printf("Invalid operands for && operator, at line %d, character %d\n", token.line, token.column);
            hasError = true;
        }

        if (!e1str.equals("boolean") ||
            !e2str.equals("boolean")) {
            MJToken token = location.get(n);
            System.out.printf("Non-boolean operand for operator && at line %d, character %d\n",
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
        // Check if the identifier is a method/class.
        if(environment.hasId(n.s) && environment.get(n.s) instanceof VariableAttribute){
            VariableAttribute var = (VariableAttribute)environment.get(n.s);
            return var.getType();
        }
        return n.s;
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
        // Check integer for expresions.
        String e1str = n.e.accept(this);
        if(hasBadIdentifier(n.e, e1str)){
            MJToken token = location.get(n.e);
            System.out.printf("Invalid operands for ! operator, at line %d, character %d\n", token.line, token.column);
            hasError = true;
        }

        if (!e1str.equals("boolean")) {
            MJToken token = location.get(n);
            System.out.printf("Non-boolean operand for operator ! at line %d, character %d\n",
                              token.line, token.column);
            hasError = true;
            return "";
        }

        return "boolean";
    }
    public String visit(ArrayLength n) {
        // Make sure int[] is used.
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
        // Check integer for expresions.
        String e1str = n.e1.accept(this);
        String e2str = n.e2.accept(this);
        if(hasBadIdentifier(n.e1, e1str)){
            MJToken token = location.get(n.e1);
            System.out.printf("Invalid operands for < operator, at line %d, character %d\n", token.line, token.column);
            hasError = true;
        }
        if(hasBadIdentifier(n.e2, e2str)){
            MJToken token = location.get(n.e2);
            System.out.printf("Invalid operands for < operator, at line %d, character %d\n", token.line, token.column);
            hasError = true;
        }

        if (!e1str.equals("int") ||
            !e2str.equals("int")) {
            MJToken token = location.get(n);
            System.out.printf("Non-integer operand for operator < at line %d, character %d\n",
                              token.line, token.column);
            hasError = true;
            return "";
        }

        return "boolean";
     }
    public String visit(Plus n) {
        // Check integer for expresions.
        String e1str = n.e1.accept(this);
        String e2str = n.e2.accept(this);
        if(hasBadIdentifier(n.e1, e1str)){
            MJToken token = location.get(n.e1);
            System.out.printf("Invalid operands for + operator, at line %d, character %d\n", token.line, token.column);
            hasError = true;
        }
        if(hasBadIdentifier(n.e2, e2str)){
            MJToken token = location.get(n.e2);
            System.out.printf("Invalid operands for + operator, at line %d, character %d\n", token.line, token.column);
            hasError = true;
        }

        if (!e1str.equals("int") ||
            !e2str.equals("int")) {
            MJToken token = location.get(n);
            System.out.printf("Non-integer operand for operator + at line %d, character %d\n",
                              token.line, token.column);
            hasError = true;
            return "";
        }

        return "int";
    }
    public String visit(Minus n) {
        // Check integer for expresions.
        String e1str = n.e1.accept(this);
        String e2str = n.e2.accept(this);
        if(hasBadIdentifier(n.e1, e1str)){
            MJToken token = location.get(n.e1);
            System.out.printf("Invalid operands for - operator, at line %d, character %d\n", token.line, token.column);
            hasError = true;
        }
        if(hasBadIdentifier(n.e2, e2str)){
            MJToken token = location.get(n.e2);
            System.out.printf("Invalid operands for - operator, at line %d, character %d\n", token.line, token.column);
            hasError = true;
        }

        if (!e1str.equals("int") ||
            !e2str.equals("int")) {
            MJToken token = location.get(n);
            System.out.printf("Non-integer operand for operator - at line %d, character %d\n",
                              token.line, token.column);
            hasError = true;
            return "";
        }

        return "int";
    }
    public String visit(Times n) {
         // Check integer for expresions.
        String e1str = n.e1.accept(this);
        String e2str = n.e2.accept(this);
        if(hasBadIdentifier(n.e1, e1str)){
            MJToken token = location.get(n.e1);
            System.out.printf("Invalid operands for * operator, at line %d, character %d\n", token.line, token.column);
            hasError = true;
        }
        if(hasBadIdentifier(n.e2, e2str)){
            MJToken token = location.get(n.e2);
            System.out.printf("Invalid operands for * operator, at line %d, character %d\n", token.line, token.column);
            hasError = true;
        }

        if (!e1str.equals("int") ||
            !e2str.equals("int")) {
            MJToken token = location.get(n);
            System.out.printf("Non-integer operand for operator * at line %d, character %d\n",
                              token.line, token.column);
            hasError = true;
            return "";
        }

        return "int";
     }

    public String visit(Identifier n) {
        return n.s;
    }

    //Statement work.
    public void visit(Block n){
        for(int i = 0; i < n.sl.size(); i++){
            n.sl.elementAt(i).accept(this);
        }
    }
    public void visit(If n){
        // boolean evaluation.
        if (!n.e.accept(this).equals("boolean")) {
            MJToken token = location.get(n);
            System.out.printf("Non-boolean expression used as the condition of if statement at line %d, character %d\n",
                              token.line, token.column);
            hasError = true;
        }
        n.s1.accept(this);
        n.s2.accept(this);
    }
    public void visit(While n){
        // Make sure boolean evaluation.
        if (!n.e.accept(this).equals("boolean")) {
            MJToken token = location.get(n);
            System.out.printf("Non-boolean expression used as the condition of while statement at line %d, character %d\n",
                              token.line, token.column);
            hasError = true;
            return;
        }
        n.s.accept(this);
    }
    public void visit(Print n) {
        MethodAttribute meth = (MethodAttribute)environment.get("System.out.println");
        // Note: The number of arguments will always match (since it's not a list)

        // Check to see if the type of the argument doesn't match.
        String type = n.e.accept(this);
        VariableAttribute var = meth.getParameter(0);
        if(!type.equals(var.getType())){
            MJToken token = location.get(n);
            System.out.printf("Call of method %s does not match its declared signature at line %d, character %d\n", "System.out.println", token.line, token.column);
            hasError = true;
        }
    }
    public void visit(Assign n){
        String exprType = n.e.accept(this);
        if(n.e instanceof IdentifierExp && environment.hasId(exprType) && !(environment.get(exprType) instanceof VarDecl)){
            MJToken token = location.get(n.i);
            if(environment.get(exprType) instanceof MethodAttribute){
                System.out.printf("Invalid r-value, %s is a %s, at line %d, character %d\n", exprType, "Method", token.line, token.column);
            }
            else{
                System.out.printf("Invalid r-value, %s is a %s, at line %d, character %d\n", exprType, "Class", token.line, token.column);
            }
            hasError = true;

        }

        if(!environment.hasId(n.i.s)){
            MJToken token = location.get(n.i);
            System.out.printf("Use of undefined identifier %s at line %d, character %d\n", n.i.s, token.line, token.column);
            hasError = true;
            return;
        }

        Attribute attr = (Attribute)environment.get(n.i.s);
        //Check for left value assignment of this or class/method name.
        if (n.i.s.equals("this") || !(attr instanceof VariableAttribute)) {
            MJToken token = location.get(n);
            String type = "reference"; // FIXME: what do we call a 'this'? 'this'.
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

        // right hand type == left hand type, accounting for polymorphism
        String identifierType = ((VariableAttribute)attr).getType();
        if (!exprType.equals(identifierType) && !isSubclassOf(exprType, identifierType)) {
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
            MJToken token = location.get(n.i);
            System.out.printf("Use of undefined identifier %s at line %d, character %d\n", n.i.s, token.line, token.column);
            hasError = true;
            return;
        }
        //Check for left value assignment of this or class/method name.
        if(n.i.s.equalsIgnoreCase("this") || !(environment.get(n.i.s) instanceof VariableAttribute)){
            MJToken token = location.get(n);
            String type = "reference"; // FIXME: what do we call a 'this'? 'this'
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
        VariableAttribute var = (VariableAttribute)environment.get(n.i.s);
        MJToken token = location.get(n.i);
        if(!"int".equals(n.e1.accept(this))){
            System.out.printf("Warning, not using an integer for accessing an array at line %d, character %d\n", token.line, token.column);
        }
        if(!"int".equals(n.e2.accept(this))){
            System.out.printf("Type mismatch during assignment at line %d, character %d\n", token.line, token.column);
            hasError = true;
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
