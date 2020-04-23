package lambda.rodeo.lang.types;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Atom implements Type {

  public static final Atom UNDEFINED_VAR = new Atom("$UNDEFINED");

  private final String atom;

  public Atom(String atom) {
    this.atom = atom;
  }

  @Override
  public String toString() {
    return ":" + atom;
  }
}
