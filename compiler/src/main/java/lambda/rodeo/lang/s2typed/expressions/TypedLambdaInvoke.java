package lambda.rodeo.lang.s2typed.expressions;

import java.util.List;
import java.util.stream.Collectors;
import lambda.rodeo.lang.compilation.CollectsErrors;
import lambda.rodeo.lang.s1ast.expressions.ExpressionAst;
import lambda.rodeo.lang.s3compileable.expression.CompileableExpr;
import lambda.rodeo.lang.s3compileable.expression.CompileableLambdaInvoke;
import lambda.rodeo.lang.scope.TypedModuleScope;
import lambda.rodeo.lang.types.CompileableLambdaType;
import lambda.rodeo.lang.types.CompileableType;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
public class TypedLambdaInvoke implements TypedExpression {

  @NonNull
  private final ExpressionAst expr;
  @NonNull
  private final CompileableType type;
  @NonNull
  private final CompileableLambdaType lambda;
  @NonNull
  private final List<TypedExpression> args;
  @NonNull
  private final TypedModuleScope typedModuleScope;

  private final TypedExpression invokeTarget;

  @Override
  public CompileableExpr toCompileableExpr(
      CollectsErrors compileContext) {
    return CompileableLambdaInvoke.builder()
        .invokeTarget(invokeTarget.toCompileableExpr(compileContext))
        .args(args.stream().map(
            typedExpression -> typedExpression
                .toCompileableExpr(compileContext))
            .collect(Collectors.toList()))
        .typedExpression(this)
        .build();
  }
}
