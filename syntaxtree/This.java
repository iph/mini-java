package syntaxtree;
import visitor.*;

public class This extends Exp {
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
