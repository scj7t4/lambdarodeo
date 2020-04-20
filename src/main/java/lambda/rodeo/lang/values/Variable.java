package lambda.rodeo.lang.values;

import lambda.rodeo.lang.expressions.Scope;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Variable<T> implements Computable<T> {
  private final String name;

  @Override
  public String toString() {
    return name;
  }

  @Override
  public T compute(Scope scope) {
    throw new UnsupportedOperationException("Not implemented yet");
  }
}
