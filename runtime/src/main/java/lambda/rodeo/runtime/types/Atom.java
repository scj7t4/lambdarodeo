package lambda.rodeo.runtime.types;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Atom implements Type, CompileableType {

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
  public Class<?> javaType() {
    return Atom.class;
  }

  @Override
  public CompileableType toCompileableType() {
    return this;
  }

  @Override
  public Type getType() {
    return this;
  }
}
