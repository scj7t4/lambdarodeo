package lambda.rodeo.lang.s2typed.functions.patterns;

import lambda.rodeo.lang.s1ast.functions.patterns.VariableCaseArgAst;
import lambda.rodeo.lang.s3compileable.functions.patterns.CompileableCaseArg;
import lambda.rodeo.lang.s3compileable.functions.patterns.VariableCompileableCaseArg;
import lambda.rodeo.lang.scope.TypeScope.Entry;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class VariableTypedCaseArg implements TypedCaseArg {
  private final VariableCaseArgAst caseArgAst;
  private final Entry referencedArgEntry;

  @Override
  public CompileableCaseArg toCompileableCaseArg() {
    return VariableCompileableCaseArg.builder()
        .typedCaseArg(this)
        .build();
  }
}
