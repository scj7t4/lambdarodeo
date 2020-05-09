package lambda.rodeo.lang.s1ast.functions.patterns;

import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.s2typed.functions.patterns.IntLiteralTypedCaseArg;
import lambda.rodeo.lang.s2typed.functions.patterns.TypedCaseArg;
import lambda.rodeo.lang.scope.TypeScope;
import lambda.rodeo.lang.scope.TypedModuleScope;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class IntLiteralCaseArgAst implements CaseArgAst {
  private final String value;
  private final int startLine;
  private final int endLine;
  private final int characterStart;

  @Override
  public TypedCaseArg toTypedCaseArg(TypeScope initialTypeScope,
      TypedModuleScope typedModuleScope,
      CompileContext compileContext) {
    return IntLiteralTypedCaseArg.builder()
        .caseArgAst(this)
        .build();
  }
}
