package lambda.rodeo.lang.values;

import lambda.rodeo.lang.statement.Scope;
import lambda.rodeo.lang.statement.TypedValue;
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
    TypedValue typedValue = scope.get(name)
        .orElseThrow(() -> new UnsupportedOperationException("TODO: Better error for illegal"));
    @SuppressWarnings("unchecked")
    T value = (T) typedValue.getValue();
    return value;
  }
}
