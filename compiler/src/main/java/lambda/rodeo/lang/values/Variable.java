package lambda.rodeo.lang.values;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class Variable<T> {

  private final String name;

  @Override
  public String toString() {
    return name;
  }

}
