package lambda.rodeo.lang.s2typed.functions.patterns;

import lambda.rodeo.lang.s1ast.functions.patterns.AtomCaseArgAst;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AtomTypedCaseArg implements TypedCaseArg {
  private final AtomCaseArgAst caseArgAst;
}
