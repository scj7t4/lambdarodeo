package lambda.rodeo.lang.types;

import lambda.rodeo.lang.s1ast.expressions.AtomAst;
import lambda.rodeo.lang.s1ast.functions.Result;
import lambda.rodeo.lang.s2typed.expressions.SimpleTypedExpression;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Atom implements Type, Result, CompileableType {

  public static final Atom UNDEFINED = new Atom("$UNDEFINED");
  public static final Atom NULL = new Atom("null");

  private final String atom;

  public Atom(String atom) {
    this.atom = atom;
  }

  @Override
  public String toString() {
    return ":" + atom;
  }

  public String getNameLiteral() {
    return this.atom;
  }

  @Override
  public Atom get() {
    return this;
  }

  @Override
  public Class<?> javaType() {
    return Atom.class;
  }

  @Override
  public CompileableType toCompileableType() {
    return this;
  }

  public SimpleTypedExpression toTypedExpression() {
    return AtomAst.builder()
        .atom(this)
        .build()
        .toTypedExpression();
  }

  @Override
  public Type getType() {
    return this;
  }
}
