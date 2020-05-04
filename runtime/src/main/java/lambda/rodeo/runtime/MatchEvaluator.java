package lambda.rodeo.runtime;

import lambda.rodeo.runtime.exceptions.RuntimeCriticalLanguageException;
import lombok.Builder;

@Builder
public class MatchEvaluator {

  private final Matcher[] matchers;

  public boolean checkPattern(Object... args) {
    if (args.length != matchers.length) {
      throw new RuntimeCriticalLanguageException(
          "Matcher got too many arguments, expected " + matchers.length
              + ", got " + args.length);
    }
    for (int i = 0; i < args.length; i++) {
      if (!matchers[i].matches(args[i])) {
        return false;
      }
    }
    return true;
  }
}
