package lambda.rodeo.lang.values;

import lambda.rodeo.lang.types.Type;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Variable<T> implements ValueHolder<T> {
  private final String name;
  private final T value;

  public Variable<T> assign(T newValue) {
    return Variable.<T>builder()
        .name(name)
        .value(newValue)
        .build();
  }

  @Override
  public String toString() {
    return name;
  }
}
