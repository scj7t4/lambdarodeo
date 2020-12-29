package lambda.rodeo.runtime.patterns.matchers;

import static lambda.rodeo.runtime.execution.Trampoline.exhaust;

import java.util.Objects;
import lambda.rodeo.runtime.patterns.Matcher;

public class EqualMatcher implements Matcher {
  private final Object toMatch;

  public EqualMatcher(Object toMatch) {
    this.toMatch = toMatch;
  }

  @Override
  public boolean matches(Object input) {
    return Objects.equals(toMatch, input) || Objects.equals(exhaust(toMatch), exhaust(input));
  }
}
