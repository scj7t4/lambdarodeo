package lambda.rodeo.lang.s2typed.functions.patterns;

import lambda.rodeo.lang.s1ast.functions.patterns.VariableCaseArgAst;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class VariableTypedCaseArg implements TypedCaseArg {
  private final VariableCaseArgAst caseArgAst;
}
