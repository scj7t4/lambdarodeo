package lambda.rodeo.lang.values;

import lambda.rodeo.lang.statements.Scope;
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
  //TODO better error here
  public T compute(Scope scope) {
    @SuppressWarnings("unchecked")
    T value = (T) scope.get(name)
        .orElseThrow(() -> new UnsupportedOperationException("TODO: Better error for illegal"));
    return value;
  }
}
