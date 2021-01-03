package lambda.rodeo.lang.s1ast.functions.patterns;

import lambda.rodeo.lang.s1ast.functions.ToTypedFunctionContext;
import lambda.rodeo.lang.s2typed.functions.patterns.TypeTypedCaseArg;
import lambda.rodeo.lang.s2typed.functions.patterns.TypedCaseArg;
import lambda.rodeo.lang.scope.TypeResolver;
import lambda.rodeo.lang.scope.TypeScope;
import lambda.rodeo.lang.types.LambdaRodeoType;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TypeArgAst implements CaseArgAst {

  private final LambdaRodeoType type;
  private final int startLine;
  private final int endLine;
  private final int characterStart;

  @Override
  public TypedCaseArg toTypedCaseArg(TypeScope initialTypeScope, TypeResolver typeResolver,
      ToTypedFunctionContext compileContext) {
    return TypeTypedCaseArg.builder()
        .caseArgAst(this)
        .build();
  }
}
