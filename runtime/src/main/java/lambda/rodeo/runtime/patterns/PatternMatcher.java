package lambda.rodeo.runtime.patterns;

public class PatternMatcher {
  public static MatchEvaluator makeEvaluator(Matcher... matchers) {
    return MatchEvaluator.builder()
        .matchers(matchers)
        .build();
  }
}
