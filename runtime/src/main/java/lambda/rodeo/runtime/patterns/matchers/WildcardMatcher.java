package lambda.rodeo.runtime.patterns.matchers;

import lambda.rodeo.runtime.patterns.Matcher;

public class WildcardMatcher implements Matcher {

  @Override
  public boolean matches(Object input) {
    return true;
  }
}
