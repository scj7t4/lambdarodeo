package lambda.rodeo.lang.types;

import lambda.rodeo.lang.expressions.ConstantExpr;
import lambda.rodeo.lang.functions.Result;
import lambda.rodeo.lang.values.Computable;
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

  public ConstantExpr<Atom> toConstantExpr() {
    return ConstantExpr.<Atom>builder()
        .type(this)
        .computable(toComputable())
        .build();
  }

  public Computable<Atom> toComputable() {
    return (typeScope) -> this;
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
}
