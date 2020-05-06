package lambda.rodeo.lang.s2typed.functions.patterns;

import lambda.rodeo.lang.s1ast.functions.patterns.VariableCaseArgAst;
import lambda.rodeo.lang.s1ast.functions.patterns.WildcardCaseArgAst;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class WildcardTypedCaseArg implements TypedCaseArg {
  private final WildcardCaseArgAst caseArgAst;
}
