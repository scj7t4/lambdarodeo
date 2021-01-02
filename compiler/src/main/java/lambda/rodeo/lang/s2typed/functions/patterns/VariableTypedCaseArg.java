package lambda.rodeo.lang.s2typed.functions.patterns;

import lambda.rodeo.lang.compilation.CollectsErrors;
import lambda.rodeo.lang.s1ast.functions.patterns.VariableCaseArgAst;
import lambda.rodeo.lang.s3compileable.functions.patterns.CompileableCaseArg;
import lambda.rodeo.lang.s3compileable.functions.patterns.VariableCompileableCaseArg;
import lambda.rodeo.lang.scope.Entry;
import lambda.rodeo.lang.scope.TypedModuleScope;
import lambda.rodeo.runtime.patterns.Matcher;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class VariableTypedCaseArg implements TypedCaseArg {
  private final VariableCaseArgAst caseArgAst;
  private final Entry referencedArgEntry;

  @Override
  public CompileableCaseArg toCompileableCaseArg(
      TypedStaticPattern staticPattern, TypedModuleScope scope,
      CollectsErrors compileContext) {
    return VariableCompileableCaseArg.builder()
        .staticPattern(staticPattern)
        .typedCaseArg(this)
        .build();
  }

  @Override
  public Class<? extends Matcher> getStaticMatcherClass() {
    return null;
  }
}
