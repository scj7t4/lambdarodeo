package lambda.rodeo.lang.s1ast.functions.patterns;

import lambda.rodeo.lang.s2typed.functions.patterns.AtomTypedCaseArg;
import lambda.rodeo.lang.s2typed.functions.patterns.TypedCaseArg;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AtomCaseArgAst implements CaseArgAst {
  private final String atom;

  @Override
  public TypedCaseArg toTypedCaseArg() {
    return AtomTypedCaseArg.builder()
        .caseArgAst(this)
        .build();
  }
}
