package lambda.rodeo.lang.types;

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
}
