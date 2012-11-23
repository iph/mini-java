import syntaxtree.*;
import visitor.*;
import java.util.*;
import tools.MJToken;

public class SemanticChecker implements SemanticVisitor {
    private SymbolTable environment;
    private HashMap<Object, MJToken> location; //Still need this!
    //A dumb way to figure out when a statement is in main.
    private boolean inMain, hasError;

    public SemanticChecker(SymbolTable s, HashMap<Object, MJToken> l){
        location = l;
        environment = s;
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

        //TRAVERSAL!!!
        for(int i = 0; i < n.ml.size(); i++){
            n.ml.elementAt(i).accept(this);
        }

        //Teardown
        environment.endScope();
    }
    public void visit(ClassDeclExtends n){
        //TODO: STILL NEED TO FIGURE OUT EXTENDS
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
        //TODO: Check to see if num arguments match.
        //TODO: Check to see if the type of an argument doesn't match.
    }
    public String visit(And n) {
        //TODO: Check boolean in expressions.
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
            System.out.printf("Invalid r-value: %s is a %s, at line %d, character %d\n");
            hasError = true;
        }
        return ((VariableAttribute)attr).getType();
    }
    public String visit(This n) {
        if(inMain){
            MJToken token = location.get(n);
            System.out.printf("Illegal use of keyword ‘this’ in static method at line %d, character %d\n",
                             token.line, token.column);
            hasError = true;
        }
    }
    public String visit(NewArray n){
        return "int array"
    }
    public String visit(NewObject n){}
    public String visit(Not n){
        //TODO: Check boolean in expressions.
    }
    public String visit(ArrayLength n) {
        if (!n.e.accept(this).equals("int array")) {
            MJToken token = location.get(n);
            System.out.printf("Length property only applies to arrays, line %d, character %d\n",
                             token.line, token.column);
            hasError = true;
        }
        //TODO: Make sure int[] is used.
    }
    public String visit(LessThan n) {
        //TODO: Check integer for expressions
    }
    public String visit(Plus n) {
        //TODO: Check integer for expressions.
    }
    public String visit(Minus n) {
        //TODO: Check integer for expresions.
    }
    public String visit(Times n) {
        //TODO: Check integer for expressions.
    }

    public String visit(Identifier n) {

    }

    //Statement work.
    //TODO: Again, statement scope is already defined. It is a matter of
    //      implementing the checks on expression and individual statements.
    public void visit(Block n){}
    public void visit(If n){
        if (!n.e.accept(this).equals("boolean")) {
            MJToken token = location.get(n);
            System.out.printf("Non-boolean expression used as the condition of if statement at line %d, character %d\n",
                              token.line, token.column);
            hasError = true;
            return;
        }
        //TODO: Make sure boolean evaluation.
    }
    public void visit(While n){
        if (!n.e.accept(this).equals("boolean")) {
            MJToken token = location.get(n);
            System.out.printf("Non-boolean expression used as the condition of while statement at line %d, character %d\n",
                              token.line, token.column);
            hasError = true;
            return;
        }
        //TODO: Make sure boolean evaluation.
    }
    public void visit(Print n){}
    public void visit(Assign n){
        n.e.accept(this);
        if(!environment.hasId(n.i.s)){
            System.out.printf("WHAT YOU DOING STUPID?\n");
            hasError = true;
            return;
        }
        //Check for left value assignment of this or class/method name.
        if(n.i.s.equalsIgnoreCase("this") || !(environment.get(n.i.s) instanceof VariableAttribute)){
           System.out.print("Invalid l-value, %s is a %s, at line %d, character %d\n");
           hasError = true;
           return;
        }

        //TODO: Make sure right hand type == left hand type.
    }
    public void visit(ArrayAssign n){
        //TODO: Check for left value assignment of this or class/method name.
    }

    //Uhh...No work? Maybe we can do types...
    public String visit(VarDecl n){}
    public String visit(IntArrayType n){}
    public String visit(BooleanType n){}
    public String visit(IntegerType n){}
    public String visit(IdentifierType n){}
    public String visit(Formal n){}
}
