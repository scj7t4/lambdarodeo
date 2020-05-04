package lambda.rodeo.runtime.patterns.matchers;

import java.math.BigInteger;
import java.util.Objects;
import lambda.rodeo.runtime.patterns.Matcher;

public class IntMatcher implements Matcher {

  private final BigInteger toMatch;

  public IntMatcher(BigInteger match) {
    this.toMatch = match;
  }

  @Override
  public boolean matches(Object input) {
    return Objects.equals(toMatch, input);
  }
}
