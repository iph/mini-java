import syntaxtree.*;
import visitor.*;
import java.util.*;
import tools.MJToken;

public class SemanticChecker implements Visitor{
    private SymbolTable environment;
    private HashMap<Object, MJToken> location; //Still need this!

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
        //Only time we need to manually input stuff, hopefully.
        VariableAttribute strArg = new VariableAttribute(token.line, token.column, "String array");
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
    public void visit(Call n){}
    public void visit(And n){}
    public void visit(ArrayLookup n){}
    public void visit(IntegerLiteral n){}
    public void visit(True n){}
    public void visit(False n){}
    public void visit(IdentifierExp n){}
    public void visit(This n){}
    public void visit(NewArray n){}
    public void visit(NewObject n){}
    public void visit(Not n){}
    public void visit(ArrayLength n){}
    public void visit(LessThan n){}
    public void visit(Plus n){}
    public void visit(Minus n){}
    public void visit(Times n){}

    //Statement work.
    //TODO: Again, statement scope is already defined. It is a matter of
    //      implementing the checks on expression and individual statements.
    public void visit(Block n){}
    public void visit(If n){}
    public void visit(While n){}
    public void visit(Print n){}
    public void visit(Assign n){}
    public void visit(ArrayAssign n){}


    //Uhh...No work? Maybe we can do types...
    public void visit(VarDecl n){}
    public void visit(IntArrayType n){}
    public void visit(BooleanType n){}
    public void visit(IntegerType n){}
    public void visit(IdentifierType n){}
    public void visit(Identifier n){}
    public void visit(Formal n){}
    }
