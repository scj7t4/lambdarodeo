package lambda.rodeo.lang.typed.expressions;

import java.util.List;
import lambda.rodeo.lang.ast.expressions.ExpressionAst;
import lambda.rodeo.lang.ast.expressions.FunctionCallAst;
import lambda.rodeo.lang.compileable.expression.CompileableExpr;
import lambda.rodeo.lang.types.Type;
import lambda.rodeo.lang.types.TypeScope;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TypedFunctionCall implements TypedExpression {

  private final TypeScope typeScope;
  private final FunctionCallAst functionCallAst;
  private final Type returnType;
  private final List<TypedExpression> args;

  @Override
  public Type getType() {
    return returnType;
  }

  @Override
  public ExpressionAst getExpr() {
    return functionCallAst;
  }

  @Override
  public CompileableExpr toCompileableExpr() {
    return null;
  }
}
