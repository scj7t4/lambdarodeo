package lambda.rodeo.lang.s2typed.expressions;

import java.util.function.Supplier;
import lambda.rodeo.lang.s3compileable.expression.Compileable;
import lambda.rodeo.lang.s3compileable.expression.CompileableExpr;
import lambda.rodeo.lang.s1ast.expressions.ExpressionAst;
import lambda.rodeo.lang.s3compileable.expression.SimpleCompilableExpr;
import lambda.rodeo.runtime.types.CompileableType;
import lambda.rodeo.runtime.types.LambdaRodeoType;
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

  private final @NonNull Supplier<Compileable> toCompileable;


  @Override
  public CompileableExpr toCompileableExpr() {
    return SimpleCompilableExpr.builder()
        .typedExpression(this)
        .compileable(toCompileable.get())
        .build();
  }
}
