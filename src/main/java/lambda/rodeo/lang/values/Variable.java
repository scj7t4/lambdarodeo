package lambda.rodeo.lang.values;

import lambda.rodeo.lang.exceptions.CriticalLanguageException;
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
  public T compute(Scope scope) {
    @SuppressWarnings("unchecked")
    T value = (T) scope.get(name)
        .orElseThrow(() -> new CriticalLanguageException("Variable '" + name + "' is not defined"
            + "in scope"));
    return value;
  }
}
