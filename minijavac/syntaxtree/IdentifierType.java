package minijavac.syntaxtree;
import minijavac.visitor.*;

public class IdentifierType extends Type {
  public String s;

  public IdentifierType(String as) {
    s=as;
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
