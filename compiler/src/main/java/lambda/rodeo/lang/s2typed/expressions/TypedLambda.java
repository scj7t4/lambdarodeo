package lambda.rodeo.lang.s2typed.expressions;

import java.util.List;
import lambda.rodeo.lang.s1ast.expressions.ExpressionAst;
import lambda.rodeo.lang.s1ast.expressions.LambdaAst;
import lambda.rodeo.lang.s1ast.functions.TypedVar;
import lambda.rodeo.lang.s2typed.statements.TypedStatement;
import lambda.rodeo.lang.s3compileable.expression.CompileableExpr;
import lambda.rodeo.runtime.types.Type;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
public class TypedLambda implements TypedExpression {
  @NonNull
  private final LambdaAst expr;

  @NonNull
  private final List<TypedVar> scopeArgs;

  @NonNull
  private final Type type;

  @NonNull
  private final List<TypedStatement> typedStatements;

  @NonNull
  public List<TypedVar> getArguments() {
    return getExpr().getArguments();
  }

  @Override
  public CompileableExpr toCompileableExpr() {
    return null;
  }
}
