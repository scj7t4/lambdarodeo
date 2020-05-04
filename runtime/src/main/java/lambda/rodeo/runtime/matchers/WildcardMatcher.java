package lambda.rodeo.runtime.matchers;

import lambda.rodeo.runtime.Matcher;

public class WildcardMatcher implements Matcher {

  @Override
  public boolean matches(Object input) {
    return true;
  }
}
