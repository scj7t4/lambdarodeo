package lambda.rodeo.lang.values;

import lambda.rodeo.lang.types.Type;
import lombok.Builder;

@Builder
public class Constant<T> implements ValueHolder<T> {

  private final T value;

  @Override
  public T getValue() {
    return value;
  }

  @Override
  public String toString() {
    return value.toString();
  }
}
