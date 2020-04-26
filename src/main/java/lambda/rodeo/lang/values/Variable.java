package lambda.rodeo.lang.values;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Variable<T> {

  private final String name;

  @Override
  public String toString() {
    return name;
  }

}
