package lambda.rodeo.lang.values;

import lambda.rodeo.lang.expressions.Scope;
import lombok.Builder;

@Builder
public class Constant<T> implements Computable<T> {

  private final T value;

  @Override
  public T compute(Scope scope) {
    return value;
  }

  @Override
  public String toString() {
    return value.toString();
  }
}
