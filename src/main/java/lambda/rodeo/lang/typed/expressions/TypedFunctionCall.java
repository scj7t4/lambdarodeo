package lambda.rodeo.lang.typed.expressions;

import java.util.List;
import java.util.stream.Collectors;
import lambda.rodeo.lang.ast.expressions.ExpressionAst;
import lambda.rodeo.lang.ast.expressions.FunctionCallAst;
import lambda.rodeo.lang.compileable.expression.CompileableExpr;
import lambda.rodeo.lang.compileable.expression.CompileableFunctionCall;
import lambda.rodeo.lang.scope.CompileableModuleScope;
import lambda.rodeo.lang.types.Type;
import lambda.rodeo.lang.scope.TypeScope;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
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
  public CompileableExpr toCompileableExpr(CompileableModuleScope compileableModuleScope) {
    return CompileableFunctionCall.builder()
        .typedExpression(this)
        .compileableModuleScope(compileableModuleScope)
        .args(args.stream()
            .map(typedExpression -> typedExpression.toCompileableExpr(compileableModuleScope))
            .collect(Collectors.toList()))
        .build();
  }
}
