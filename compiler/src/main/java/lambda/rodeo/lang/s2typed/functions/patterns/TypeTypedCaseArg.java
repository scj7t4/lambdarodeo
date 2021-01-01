package lambda.rodeo.lang.s2typed.functions.patterns;

import lambda.rodeo.lang.compilation.CollectsErrors;
import lambda.rodeo.lang.s1ast.functions.patterns.TypeArgAst;
import lambda.rodeo.lang.s3compileable.functions.patterns.CompileableCaseArg;
import lambda.rodeo.lang.s3compileable.functions.patterns.TypeCompileableCaseArg;
import lambda.rodeo.lang.scope.TypedModuleScope;
import lambda.rodeo.runtime.patterns.Matcher;
import lambda.rodeo.runtime.patterns.matchers.TypeMatcher;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
public class TypeTypedCaseArg implements TypedCaseArg {

  @NonNull
  private final TypeArgAst caseArgAst;

  @Override
  public CompileableCaseArg toCompileableCaseArg(
      TypedStaticPattern staticPattern,
      TypedModuleScope scope,
      CollectsErrors compileContext) {
    return TypeCompileableCaseArg.builder()
        .type(caseArgAst.getType().toCompileableType(scope, compileContext))
        .staticPattern(staticPattern)
        .typedCaseArg(this)
        .build();
  }

  @Override
  public Class<? extends Matcher> getStaticMatcherClass() {
    return TypeMatcher.class;
  }
}
