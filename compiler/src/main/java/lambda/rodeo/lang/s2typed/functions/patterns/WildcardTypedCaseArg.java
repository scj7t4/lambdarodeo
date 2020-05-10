package lambda.rodeo.lang.s2typed.functions.patterns;

import lambda.rodeo.lang.s1ast.functions.patterns.WildcardCaseArgAst;
import lambda.rodeo.lang.s3compileable.functions.patterns.CompileableCaseArg;
import lambda.rodeo.lang.s3compileable.functions.patterns.WildcardCompileableCaseArg;
import lambda.rodeo.runtime.patterns.Matcher;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class WildcardTypedCaseArg implements TypedCaseArg {
  private final WildcardCaseArgAst caseArgAst;


  @Override
  public CompileableCaseArg toCompileableCaseArg(
      TypedStaticPattern staticPattern) {
    return WildcardCompileableCaseArg.builder()
        .staticPattern(staticPattern)
        .typedCaseArg(this)
        .build();
  }

  @Override
  public Class<? extends Matcher> getStaticMatcherClass() {
    return null;
  }
}
