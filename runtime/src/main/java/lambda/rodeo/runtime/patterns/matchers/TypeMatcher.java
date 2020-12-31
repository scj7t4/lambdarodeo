package lambda.rodeo.runtime.patterns.matchers;

import lambda.rodeo.runtime.patterns.Matcher;
import lambda.rodeo.runtime.types.LRType;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
public class TypeMatcher implements Matcher {

  @NonNull
  private final LRType type;

  @Override
  public boolean matches(Object input) {
    return type.isObjectOfType(input);
  }
}
