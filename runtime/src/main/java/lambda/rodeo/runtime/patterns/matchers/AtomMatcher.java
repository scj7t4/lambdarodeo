package lambda.rodeo.runtime.patterns.matchers;

import java.util.Objects;
import lambda.rodeo.runtime.patterns.Matcher;
import lambda.rodeo.runtime.types.Atom;

public class AtomMatcher implements Matcher {

  private final Atom expected;

  public AtomMatcher(Atom expected) {
    this.expected = expected;
  }

  @Override
  public boolean matches(Object input) {
    return Objects.equals(expected, input);
  }
}
