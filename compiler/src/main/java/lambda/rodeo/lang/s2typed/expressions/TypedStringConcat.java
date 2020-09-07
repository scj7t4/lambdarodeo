package lambda.rodeo.lang.s2typed.expressions;

import lambda.rodeo.lang.s1ast.expressions.ExpressionAst;
import lambda.rodeo.lang.s3compileable.expression.CompileableExpr;
import lambda.rodeo.lang.s3compileable.expression.CompileableStringConcat;
import lambda.rodeo.lang.types.CompileableType;
import lambda.rodeo.lang.types.StringType;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
public class TypedStringConcat implements TypedExpression {
  @NonNull
  private final TypedExpression lhs;
  @NonNull
  private final TypedExpression rhs;
  @NonNull
  private final ExpressionAst expr;

  @Override
  public CompileableType getType() {
    return StringType.INSTANCE;
  }

  @Override
  public CompileableExpr toCompileableExpr() {
    return CompileableStringConcat.builder()
        .lhs(lhs.toCompileableExpr())
        .rhs(rhs.toCompileableExpr())
        .typedExpression(this)
        .build();
  }
}
