package syntaxtree;
import visitor.*;

public abstract class Type {
  public abstract void accept(Visitor v);
  public abstract Type accept(TypeVisitor v);
  public abstract void accept(IRVisitor v);
  public abstract String accept(SemanticVisitor v);
}
