package minijavac.syntaxtree;
import minijavac.visitor.*;

public class NewObject extends Exp {
  public Identifier i;
  
  public NewObject(Identifier ai) {
    i=ai;
  }

  public void accept(Visitor v) {
    v.visit(this);
  }

  public Type accept(TypeVisitor v) {
    return v.visit(this);
  }

  public String accept(IRVisitor v) {
    return v.visit(this);
  }

  public String accept(SemanticVisitor v) {
    return v.visit(this);
  }
}
