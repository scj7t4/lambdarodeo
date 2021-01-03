package lambda.rodeo.lang.s2typed.expressions;

import java.util.List;
import java.util.stream.Collectors;
import lambda.rodeo.lang.compilation.CollectsErrors;
import lambda.rodeo.lang.s1ast.expressions.ExpressionAst;
import lambda.rodeo.lang.s1ast.expressions.FunctionCallAst;
import lambda.rodeo.lang.s3compileable.expression.CompileableExpr;
import lambda.rodeo.lang.s3compileable.expression.CompileableFunctionCall;
import lambda.rodeo.lang.scope.TypeResolver;
import lambda.rodeo.lang.scope.TypeScope;
import lambda.rodeo.lang.scope.TypedModuleScope;
import lambda.rodeo.lang.types.CompileableType;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
@EqualsAndHashCode
public class TypedFunctionCall implements TypedExpression {

  @NonNull
  private final TypeScope typeScope;
  @NonNull
  private final FunctionCallAst functionCallAst;
  @NonNull
  private final String callTarget;
  @NonNull
  private final CompileableType returnType;
  @NonNull
  private final List<TypedExpression> args;
  @NonNull
  private final TypedModuleScope typedModuleScope;

  @Override
  public CompileableType getType() {
    return returnType;
  }

  @Override
  public ExpressionAst getExpr() {
    return functionCallAst;
  }

  @Override
  public CompileableExpr toCompileableExpr(
      CollectsErrors compileContext) {
    return CompileableFunctionCall.builder()
        .typedExpression(this)
        .args(args.stream()
            .map(typedExpression -> typedExpression.toCompileableExpr(compileContext))
            .collect(Collectors.toList()))
        .build();
  }
}
