package minijavac.syntaxtree;
import minijavac.visitor.*;

public class Identifier {
  public String s;

  public Identifier(String as) { 
    s=as;
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
  
  public String toString(){
    return s;
  }
}
