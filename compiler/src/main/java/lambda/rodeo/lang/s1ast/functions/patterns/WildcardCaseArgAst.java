package lambda.rodeo.lang.s1ast.functions.patterns;

import lambda.rodeo.lang.s2typed.functions.patterns.AtomTypedCaseArg;
import lambda.rodeo.lang.s2typed.functions.patterns.TypedCaseArg;
import lambda.rodeo.lang.s2typed.functions.patterns.WildcardTypedCaseArg;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class WildcardCaseArgAst implements CaseArgAst {

  @Override
  public TypedCaseArg toTypedCaseArg() {
    return WildcardTypedCaseArg.builder()
        .caseArgAst(this)
        .build();
  }
}
