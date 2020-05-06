package lambda.rodeo.lang.s1ast.functions.patterns;

import lambda.rodeo.lang.s2typed.functions.patterns.AtomTypedCaseArg;
import lambda.rodeo.lang.s2typed.functions.patterns.TypedCaseArg;
import lambda.rodeo.lang.s2typed.functions.patterns.VariableTypedCaseArg;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class VariableCaseArgAst implements CaseArgAst {
  private final String identifier;

  @Override
  public TypedCaseArg toTypedCaseArg() {
    return VariableTypedCaseArg.builder()
        .caseArgAst(this)
        .build();
  }
}
