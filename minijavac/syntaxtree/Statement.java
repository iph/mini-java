package minijavac.syntaxtree;
import minijavac.visitor.*;

public abstract class Statement {
  public abstract void accept(Visitor v);
  public abstract Type accept(TypeVisitor v);
  public abstract void accept(IRVisitor v);
  public abstract void accept(SemanticVisitor v);
}
