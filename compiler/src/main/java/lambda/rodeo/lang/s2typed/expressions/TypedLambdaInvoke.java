package lambda.rodeo.lang.s2typed.expressions;

import java.util.List;
import lambda.rodeo.lang.s1ast.expressions.ExpressionAst;
import lambda.rodeo.lang.s1ast.expressions.FunctionCallAst;
import lambda.rodeo.lang.s3compileable.expression.CompileableExpr;
import lambda.rodeo.lang.scope.TypeScope.Entry;
import lambda.rodeo.lang.scope.TypedModuleScope;
import lambda.rodeo.runtime.types.LambdaRodeoType;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
public class TypedLambdaInvoke implements TypedExpression {

  @NonNull
  private final ExpressionAst expr;
  @NonNull
  private final LambdaRodeoType type;
  @NonNull
  private final List<TypedExpression> args;
  @NonNull
  private final TypedModuleScope typedModuleScope;
  
  private final Entry invokeTarget;

  @Override
  public CompileableExpr toCompileableExpr() {
    return null;
  }
}
