package lambda.rodeo.lang.s2typed.expressions;

import java.util.function.Supplier;
import lambda.rodeo.lang.compilation.CollectsErrors;
import lambda.rodeo.lang.s1ast.expressions.ExpressionAst;
import lambda.rodeo.lang.s3compileable.expression.CompileableExpr;
import lambda.rodeo.lang.s3compileable.expression.CompileableExpression;
import lambda.rodeo.lang.s3compileable.expression.SimpleCompilableExpr;
import lambda.rodeo.lang.types.CompileableType;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class SimpleTypedExpression implements TypedExpression {

  @NonNull
  private final ExpressionAst expr;

  @NonNull
  private final CompileableType type;

  private final @NonNull Supplier<CompileableExpression> toCompileable;


  @Override
  public CompileableExpr toCompileableExpr(
      CollectsErrors compileContext) {
    return SimpleCompilableExpr.builder()
        .typedExpression(this)
        .compileable(toCompileable.get())
        .build();
  }
}
