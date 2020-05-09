package lambda.rodeo.lang.s2typed.functions.patterns;

import lambda.rodeo.lang.s1ast.functions.patterns.IntLiteralCaseArgAst;
import lambda.rodeo.lang.s3compileable.functions.patterns.CompileableCaseArg;
import lambda.rodeo.lang.s3compileable.functions.patterns.IntLiteralCompileableCaseArg;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class IntLiteralTypedCaseArg implements TypedCaseArg {
  private final IntLiteralCaseArgAst caseArgAst;

  @Override
  public CompileableCaseArg toCompileableCaseArg() {
    return IntLiteralCompileableCaseArg.builder()
        .typedCaseArg(this)
        .build();
  }
}
