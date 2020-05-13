package lambda.rodeo.lang.s1ast.expressions;

import java.util.List;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.s1ast.functions.TypedVar;
import lambda.rodeo.lang.s1ast.statements.StatementAst;
import lambda.rodeo.lang.s2typed.expressions.TypedExpression;
import lambda.rodeo.lang.scope.TypeScope;
import lambda.rodeo.lang.scope.TypedModuleScope;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LambdaAst implements ExpressionAst {
  private final int startLine;
  private final int endLine;
  private final int characterStart;

  private List<TypedVar> arguments;
  private List<StatementAst> statements;

  @Override
  public TypedExpression toTypedExpression(TypeScope scope, TypedModuleScope typedModuleScope,
      CompileContext compileContext) {
    return null;
  }
}
