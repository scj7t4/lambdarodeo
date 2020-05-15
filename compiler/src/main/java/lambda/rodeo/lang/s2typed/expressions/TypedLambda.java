package lambda.rodeo.lang.s2typed.expressions;

import java.util.Collections;
import java.util.List;
import lambda.rodeo.lang.s1ast.expressions.LambdaAst;
import lambda.rodeo.lang.s1ast.functions.TypedVar;
import lambda.rodeo.lang.s2typed.functions.TypedFunction;
import lambda.rodeo.lang.s3compileable.expression.CompileableExpr;
import lambda.rodeo.lang.s3compileable.expression.CompileableLambda;
import lambda.rodeo.lang.s3compileable.functions.CompileableFunction;
import lambda.rodeo.lang.scope.TypeScope.Entry;
import lambda.rodeo.lang.scope.TypedModuleScope;
import lambda.rodeo.runtime.types.Lambda;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
public class TypedLambda implements TypedExpression {

  @NonNull
  private final LambdaAst expr;

  @NonNull
  private final List<Entry> scopeArgs;

  @NonNull
  private final Lambda type;

  @NonNull
  private final TypedFunction typedFunction;

  @NonNull
  private final TypedModuleScope typedModuleScope;

  @NonNull
  public List<TypedVar> getArguments() {
    return getExpr().getArguments();
  }

  @Override
  public CompileableExpr toCompileableExpr() {
    CompileableFunction function = typedFunction
        .toCompileableFunction(Collections.emptyMap());

    return CompileableLambda.builder()
        .lambdaFunction(function)
        .typedExpression(this)
        .build();
  }
}
