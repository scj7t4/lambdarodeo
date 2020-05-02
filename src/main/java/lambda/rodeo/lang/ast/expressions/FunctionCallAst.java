package lambda.rodeo.lang.ast.expressions;

import java.util.List;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.typed.expressions.SimpleTypedExpression;
import lambda.rodeo.lang.types.TypeScope;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FunctionCallAst implements ExpressionAst{

  private final String callTarget;
  private final List<ExpressionAst> args;
  private final int startLine;
  private final int endLine;
  private final int characterStart;

  @Override
  public SimpleTypedExpression toTypedExpressionAst(TypeScope typeScope, CompileContext compileContext) {
    return null; //TODO determine type
  }
}
