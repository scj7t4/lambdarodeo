package lambda.rodeo.lang.s1ast.functions.patterns;

import lambda.rodeo.lang.s1ast.functions.ToTypedFunctionContext;
import lambda.rodeo.lang.s2typed.functions.patterns.TypedCaseArg;
import lambda.rodeo.lang.s2typed.functions.patterns.WildcardTypedCaseArg;
import lambda.rodeo.lang.scope.TypeResolver;
import lambda.rodeo.lang.scope.TypeScope;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class WildcardCaseArgAst implements CaseArgAst {

  private final int startLine;
  private final int endLine;
  private final int characterStart;

  @Override
  public TypedCaseArg toTypedCaseArg(TypeScope initialTypeScope,
      TypeResolver typeResolver,
      ToTypedFunctionContext compileContext) {
    return WildcardTypedCaseArg.builder()
        .caseArgAst(this)
        .build();
  }
}
