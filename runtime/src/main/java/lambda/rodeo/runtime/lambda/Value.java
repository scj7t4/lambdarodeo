package lambda.rodeo.runtime.lambda;

import java.math.BigInteger;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Value<T> implements Lambda0<T> {
  private final T contents;

  Value(T contents) {
    this.contents = contents;
  }

  public T apply() {
    return contents;
  }

  public String toString() {
    return contents.toString();
  }

  public static <T> Value<T> of(T contents) {
    return new Value<>(contents);
  }

  public static Value<BigInteger> of(long v) {
    return of(BigInteger.valueOf(v));
  }
}
