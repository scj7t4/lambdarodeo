package lambda.rodeo.lang.s2typed.functions.patterns;

import lambda.rodeo.lang.compilation.CollectsErrors;
import lambda.rodeo.lang.s1ast.functions.patterns.IntLiteralCaseArgAst;
import lambda.rodeo.lang.s3compileable.functions.patterns.CompileableCaseArg;
import lambda.rodeo.lang.s3compileable.functions.patterns.IntLiteralCompileableCaseArg;
import lambda.rodeo.lang.scope.TypeResolver;
import lambda.rodeo.runtime.patterns.Matcher;
import lambda.rodeo.runtime.patterns.matchers.IntMatcher;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class IntLiteralTypedCaseArg implements TypedCaseArg {
  private final IntLiteralCaseArgAst caseArgAst;

  @Override
  public CompileableCaseArg toCompileableCaseArg(
      TypedStaticPattern staticPattern, TypeResolver scope,
      CollectsErrors compileContext) {
    return IntLiteralCompileableCaseArg.builder()
        .staticPattern(staticPattern)
        .typedCaseArg(this)
        .build();
  }

  @Override
  public Class<? extends Matcher> getStaticMatcherClass() {
    return IntMatcher.class;
  }
}
