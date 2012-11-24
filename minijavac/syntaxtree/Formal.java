package minijavac.syntaxtree;
import minijavac.visitor.*;

public class Formal {
  public Type t;
  public Identifier i;
 
  public Formal(Type at, Identifier ai) {
    t=at; i=ai;
  }

  public void accept(Visitor v) {
    v.visit(this);
  }

  public Type accept(TypeVisitor v) {
    return v.visit(this);
  }

  public void accept(IRVisitor v) {
    v.visit(this);
  }

  public String accept(SemanticVisitor v) {
    return v.visit(this);
  }
}
