package syntaxtree;
import visitor.*;

public class BooleanType extends Type {
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
