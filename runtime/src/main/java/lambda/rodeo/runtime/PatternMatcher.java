package lambda.rodeo.runtime;

public class PatternMatcher {
  public static MatchEvaluator makeEvaluator(Matcher... matchers) {
    return MatchEvaluator.builder()
        .matchers(matchers)
        .build();
  }
}
