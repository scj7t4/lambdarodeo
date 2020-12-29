package lambda.rodeo.runtime.patterns.matchers;

import static lambda.rodeo.runtime.execution.Trampoline.trampoline;

import java.math.BigInteger;
import java.util.Objects;
import lambda.rodeo.runtime.patterns.Matcher;
import lombok.NonNull;

public class IntMatcher implements Matcher {

  @NonNull
  private final BigInteger toMatch;

  public IntMatcher(@NonNull BigInteger match) {
    this.toMatch = match;
  }

  @Override
  public boolean matches(Object input) {
    return toMatch.equals(trampoline(input));
  }
}
