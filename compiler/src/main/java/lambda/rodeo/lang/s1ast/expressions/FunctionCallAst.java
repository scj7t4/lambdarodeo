package lambda.rodeo.lang.s1ast.expressions;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lambda.rodeo.lang.compilation.CompileError;
import lambda.rodeo.lang.s1ast.functions.FunctionAst;
import lambda.rodeo.lang.s1ast.functions.ToTypedFunctionContext;
import lambda.rodeo.lang.s2typed.expressions.TypedExpression;
import lambda.rodeo.lang.s2typed.expressions.TypedFunctionCall;
import lambda.rodeo.lang.s2typed.expressions.TypedLambdaInvoke;
import lambda.rodeo.lang.scope.TypeScope;
import lambda.rodeo.lang.scope.TypeScope.Entry;
import lambda.rodeo.lang.scope.TypedModuleScope;
import lambda.rodeo.runtime.types.Atom;
import lambda.rodeo.runtime.types.Lambda;
import lambda.rodeo.runtime.types.LambdaRodeoType;
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
  public TypedExpression toTypedExpression(
      TypeScope typeScope,
      TypedModuleScope typedModuleScope,
      ToTypedFunctionContext compileContext) {

    final List<TypedExpression> typedArgs = args.stream()
        .map(arg -> arg.toTypedExpression(typeScope, typedModuleScope, compileContext))
        .collect(Collectors.toList());

    final List<LambdaRodeoType> argSig = typedArgs.stream()
        .map(TypedExpression::getType)
        .collect(Collectors.toList());

    Optional<FunctionAst> calledFn = typedModuleScope.getCallTarget(callTarget, argSig);
    if (calledFn.isEmpty()) {
      Optional<Entry> lambdaVar = typeScope.get(callTarget)
          .filter(entry -> entry.getType() instanceof Lambda)
          .filter(entry -> {
            Lambda type = (Lambda) entry.getType();
            List<? extends LambdaRodeoType> argTypes = type.getArgs();
            if (argTypes.size() != typedArgs.size()) {
              return false;
            }
            for (int i = 0; i < argTypes.size(); i++) {
              LambdaRodeoType argType = argTypes.get(i);
              LambdaRodeoType calledArg = typedArgs.get(i).getType();
              if (!argType.assignableFrom(calledArg)) {
                return false;
              }
            }
            return true;
          })
          .findFirst();
      if (lambdaVar.isPresent()) {
        Entry varEntry = lambdaVar.get();
        Lambda lambda = (Lambda) varEntry.getType();
        LambdaRodeoType returnType = lambda.getReturnType();

        return TypedLambdaInvoke.builder()
            .args(typedArgs)
            .typedModuleScope(typedModuleScope)
            .expr(this)
            .type(returnType)
            .invokeTarget(varEntry)
            .build();
      }

      String callTargetSig = callTarget + "\\\\" + argSig.size();
      compileContext.getCompileErrorCollector()
          .collect(CompileError.undefinedIdentifier(this, callTargetSig));
      return AtomAst
          .builder()
          .atom(Atom.UNDEFINED)
          .build()
          .toTypedExpression();
    }

    LambdaRodeoType declaredReturnType = calledFn.get()
        .getFunctionSignature()
        .getDeclaredReturnType();

    return TypedFunctionCall.builder()
        .args(typedArgs)
        .typedModuleScope(typedModuleScope)
        .functionCallAst(this)
        .returnType(declaredReturnType)
        .typeScope(typeScope)
        .build();

  }

  @Override
  public Set<String> getReferencedVariables() {
    return args.stream()
        .flatMap(arg -> arg.getReferencedVariables().stream())
        .collect(Collectors.toSet());
  }
}
