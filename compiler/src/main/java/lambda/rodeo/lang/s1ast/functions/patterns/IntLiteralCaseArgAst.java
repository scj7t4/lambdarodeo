package lambda.rodeo.lang.s1ast.functions.patterns;

import lambda.rodeo.lang.s2typed.functions.patterns.AtomTypedCaseArg;
import lambda.rodeo.lang.s2typed.functions.patterns.IntLiteralTypedCaseArg;
import lambda.rodeo.lang.s2typed.functions.patterns.TypedCaseArg;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class IntLiteralCaseArgAst implements CaseArgAst {
  private final String value;

  @Override
  public TypedCaseArg toTypedCaseArg() {
    return IntLiteralTypedCaseArg.builder()
        .caseArgAst(this)
        .build();
  }
}
