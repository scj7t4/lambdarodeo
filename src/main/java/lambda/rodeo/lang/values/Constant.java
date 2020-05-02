package lambda.rodeo.lang.values;

import lombok.Builder;
import lombok.EqualsAndHashCode;

@Builder
@EqualsAndHashCode
public class Constant<T> {

  private final T value;

  @Override
  public String toString() {
    return value.toString();
  }
}
