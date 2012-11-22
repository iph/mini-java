package syntaxtree;
import visitor.*;

public abstract class Exp {
  public abstract void accept(Visitor v);
  public abstract Type accept(TypeVisitor v);
  public abstract String accept(IRVisitor v);
}
