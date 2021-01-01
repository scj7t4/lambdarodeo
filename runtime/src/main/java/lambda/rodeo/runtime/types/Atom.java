package lambda.rodeo.runtime.types;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class Atom implements LRType {
  private final String atom;

  public static final Atom UNDEFINED = new Atom("$UNDEFINED");
  public static final Atom NULL = new Atom("null");

  @Override
  public String toString() {
    return "@" + atom;
  }

  @Override
  public boolean assignableFrom(LRType type) {
    return equals(type);
  }

  @Override
  public boolean isObjectOfType(Object object) {
    return Objects.equals(this, object);
  }
}
