package lambda.rodeo.lang.types;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Atom implements Type {

  private final String atom;

  public Atom(String atom) {
    this.atom = atom;
  }

  @Override
  public String toString() {
    return ":" + atom;
  }
}
