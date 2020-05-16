package lambda.rodeo.lang.s1ast.expressions;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lambda.rodeo.lang.s1ast.functions.ToTypedFunctionContext;
import lambda.rodeo.lang.s2typed.expressions.TypedExpression;
import lambda.rodeo.lang.s2typed.expressions.TypedLambda;
import lambda.rodeo.lang.s2typed.expressions.TypedLambdaInvoke;
import lambda.rodeo.lang.scope.TypeScope;
import lambda.rodeo.lang.scope.TypedModuleScope;
import lambda.rodeo.runtime.types.Lambda;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
public class LambaInvokeAst implements ExpressionAst {

  private final int startLine;
  private final int endLine;
  private final int characterStart;
  private final LambdaAst lambdaAst;
  private final List<ExpressionAst> args;

  @Override
  public TypedExpression toTypedExpression(TypeScope scope, TypedModuleScope typedModuleScope,
      ToTypedFunctionContext compileContext) {
    final List<TypedExpression> typedArgs = args.stream()
        .map(arg -> arg.toTypedExpression(scope, typedModuleScope, compileContext))
        .collect(Collectors.toList());

    TypedLambda typedExpression = lambdaAst
        .toTypedExpression(scope, typedModuleScope, compileContext);
    @NonNull Lambda type = typedExpression.getType();

    return TypedLambdaInvoke.builder()
        .args(typedArgs)
        .typedModuleScope(typedModuleScope)
        .expr(this)
        .type(type.getReturnType())
        .invokeTarget(null)
        .build();
  }

  @Override
  public Set<String> getReferencedVariables() {
    return Stream.concat(
        lambdaAst.getReferencedVariables().stream(),
        args.stream()
            .map(ExpressionAst::getReferencedVariables)
            .flatMap(Collection::stream))
        .collect(Collectors.toSet());
  }
}
