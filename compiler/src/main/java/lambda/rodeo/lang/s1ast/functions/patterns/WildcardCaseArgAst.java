package lambda.rodeo.lang.s1ast.functions.patterns;

import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.s2typed.functions.patterns.TypedCaseArg;
import lambda.rodeo.lang.s2typed.functions.patterns.WildcardTypedCaseArg;
import lambda.rodeo.lang.scope.TypeScope;
import lambda.rodeo.lang.scope.TypedModuleScope;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class WildcardCaseArgAst implements CaseArgAst {

  private final int startLine;
  private final int endLine;
  private final int characterStart;

  @Override
  public TypedCaseArg toTypedCaseArg(TypeScope initialTypeScope,
      TypedModuleScope typedModuleScope,
      CompileContext compileContext) {
    return WildcardTypedCaseArg.builder()
        .caseArgAst(this)
        .build();
  }
}
