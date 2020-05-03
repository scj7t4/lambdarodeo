package lambda.rodeo.lang.s1ast.expressions;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.compilation.CompileError;
import lambda.rodeo.lang.s1ast.functions.FunctionAst;
import lambda.rodeo.lang.s2typed.expressions.TypedExpression;
import lambda.rodeo.lang.s2typed.expressions.TypedFunctionCall;
import lambda.rodeo.lang.scope.TypedModuleScope;
import lambda.rodeo.lang.types.Atom;
import lambda.rodeo.lang.types.FunctionType;
import lambda.rodeo.lang.types.Type;
import lambda.rodeo.lang.scope.TypeScope;
import lambda.rodeo.lang.scope.TypeScope.Entry;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class FunctionCallAst implements ExpressionAst {

  private final String callTarget;
  private final List<ExpressionAst> args;
  private final int startLine;
  private final int endLine;
  private final int characterStart;

  @Override
  //TODO: Wrong...
  public TypedExpression toTypedExpression(
      TypeScope typeScope,
      TypedModuleScope typedModuleScope,
      CompileContext compileContext) {
    Optional<FunctionAst> calledFn = typedModuleScope.getCallTarget(callTarget);
    if(calledFn.isEmpty()) {
      compileContext.getCompileErrorCollector()
          .collect(CompileError.undefinedIdentifier(callTarget, this));
      return Atom.UNDEFINED.toTypedExpression();
    }

    Type declaredReturnType = calledFn.get()
        .getFunctionSignature()
        .getDeclaredReturnType();

    return TypedFunctionCall.builder()
        .args(args.stream()
            .map(arg -> arg.toTypedExpression(typeScope, typedModuleScope, compileContext))
            .collect(Collectors.toList()))
        .typedModuleScope(typedModuleScope)
        .functionCallAst(this)
        .returnType(declaredReturnType)
        .typeScope(typeScope)
        .build();

  }
}
