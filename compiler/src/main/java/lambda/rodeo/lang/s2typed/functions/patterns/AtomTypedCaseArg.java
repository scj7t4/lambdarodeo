package lambda.rodeo.lang.s2typed.functions.patterns;

import lambda.rodeo.lang.s1ast.functions.patterns.AtomCaseArgAst;
import lambda.rodeo.lang.s3compileable.functions.patterns.AtomCompileableCaseArg;
import lambda.rodeo.lang.s3compileable.functions.patterns.CompileableCaseArg;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AtomTypedCaseArg implements TypedCaseArg {
  private final AtomCaseArgAst caseArgAst;

  @Override
  public CompileableCaseArg toCompileableCaseArg() {
    return AtomCompileableCaseArg.builder()
        .typedCaseArg(this)
        .build();
  }
}
