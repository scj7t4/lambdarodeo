package lambda.rodeo.runtime.types;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class Atom {
  private final String atom;

  public static final Atom UNDEFINED = new Atom("$UNDEFINED");
  public static final Atom NULL = new Atom("null");

  @Override
  public String toString() {
    return ":" + atom;
  }
}
