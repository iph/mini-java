package minijavac.syntaxtree;
import minijavac.visitor.*;

public class ArrayLength extends Exp {
  public Exp e;
  
  public ArrayLength(Exp ae) {
    e=ae; 
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
