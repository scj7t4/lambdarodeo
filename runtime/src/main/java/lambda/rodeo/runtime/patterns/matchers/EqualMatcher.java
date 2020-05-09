package lambda.rodeo.runtime.patterns.matchers;

import java.util.Objects;
import lambda.rodeo.runtime.patterns.Matcher;

public class EqualMatcher implements Matcher {
  private Object toMatch;

  @Override
  public boolean matches(Object input) {
    return Objects.equals(toMatch, input);
  }
}
