package lambda.rodeo.runtime.matchers;

import java.math.BigInteger;
import java.util.Objects;
import lambda.rodeo.runtime.Matcher;

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
