package lambda.rodeo.lang.s2typed.functions.patterns;

import lambda.rodeo.lang.s1ast.functions.patterns.IntLiteralCaseArgAst;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class IntLiteralTypedCaseArg implements TypedCaseArg {
  private final IntLiteralCaseArgAst caseArgAst;
}
