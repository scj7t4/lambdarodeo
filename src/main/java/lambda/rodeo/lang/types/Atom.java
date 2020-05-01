package lambda.rodeo.lang.types;

import lambda.rodeo.lang.expressions.AtomAst;
import lambda.rodeo.lang.expressions.SimpleTypedExpressionAst;
import lambda.rodeo.lang.functions.Result;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Atom implements Type, Result {

  public static final Atom UNDEFINED_VAR = new Atom("$UNDEFINED");
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

  public SimpleTypedExpressionAst toTypedExpressionAst() {
    return SimpleTypedExpressionAst.builder()
        .type(this)
        .expr(AtomAst.builder()
            .atom(this)
            .build())
        .build();
  }
}
