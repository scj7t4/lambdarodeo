package lambda.rodeo.lang.s2typed.functions.patterns;

import lambda.rodeo.lang.s1ast.functions.patterns.AtomCaseArgAst;
import lambda.rodeo.lang.s3compileable.functions.patterns.AtomCompileableCaseArg;
import lambda.rodeo.lang.s3compileable.functions.patterns.CompileableCaseArg;
import lambda.rodeo.runtime.patterns.Matcher;
import lambda.rodeo.runtime.patterns.matchers.AtomMatcher;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class AtomTypedCaseArg implements TypedCaseArg {
  private final AtomCaseArgAst caseArgAst;

  @Override
  public CompileableCaseArg toCompileableCaseArg(
      TypedStaticPattern staticPattern) {
    return AtomCompileableCaseArg.builder()
        .staticPattern(staticPattern)
        .typedCaseArg(this)
        .build();
  }

  @Override
  public Class<? extends Matcher> getStaticMatcherClass() {
    return AtomMatcher.class;
  }
}
