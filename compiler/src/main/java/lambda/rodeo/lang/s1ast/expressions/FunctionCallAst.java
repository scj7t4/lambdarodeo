package lambda.rodeo.lang.s1ast.expressions;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lambda.rodeo.lang.compilation.CompileError;
import lambda.rodeo.lang.s1ast.functions.FunctionAst;
import lambda.rodeo.lang.s1ast.functions.ToTypedFunctionContext;
import lambda.rodeo.lang.s1ast.functions.TypedVar;
import lambda.rodeo.lang.s2typed.expressions.TypedExpression;
import lambda.rodeo.lang.s2typed.expressions.TypedFunctionCall;
import lambda.rodeo.lang.s2typed.expressions.TypedLambdaInvoke;
import lambda.rodeo.lang.s2typed.expressions.TypedVariable;
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

  private final ExpressionAst callTarget;
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

    if (callTarget instanceof VariableAst) {
      return handleVariableAst(typeScope, typedModuleScope, compileContext, typedArgs, argSig);
    } else {
      final List<? extends LambdaRodeoType> callArgTypes = typedArgs.stream()
          .map(TypedExpression::getType)
          .collect(Collectors.toList());
      return createInvokeForExpr(
          typeScope,
          typedModuleScope,
          compileContext,
          "<lambda>\\\\" + args.size(),
          typedArgs,
          callArgTypes);
    }
  }

  public TypedExpression handleVariableAst(
      TypeScope typeScope,
      TypedModuleScope typedModuleScope,
      ToTypedFunctionContext compileContext,
      List<TypedExpression> typedArgs,
      List<LambdaRodeoType> argSig) {
    // Get the call target Name
    String callTargetName = ((VariableAst) callTarget).getName();
    // Check to see if its in the typedModuleScope (holds functions)
    Optional<FunctionAst> calledFn = typedModuleScope.getCallTarget(callTargetName, argSig);
    final List<? extends LambdaRodeoType> callArgTypes = typedArgs.stream()
        .map(TypedExpression::getType)
        .collect(Collectors.toList());
    if (calledFn.isEmpty()) {
      // Check to see if the variable is defined in scope
      return createInvokeForExpr(
          typeScope,
          typedModuleScope,
          compileContext,
          callTargetName,
          typedArgs,
          callArgTypes);
    } else {
      FunctionAst functionAst = calledFn.get();
      LambdaRodeoType declaredReturnType = functionAst
          .getFunctionSignature()
          .getDeclaredReturnType();
      List<? extends LambdaRodeoType> argTypes = functionAst.getFunctionSignature()
          .getArguments()
          .stream()
          .map(TypedVar::getType)
          .collect(Collectors.toList());
      if (!checkCallMatchesSignature(compileContext, callArgTypes, argTypes)) {
        return AtomAst.undefinedAtomExpression();
      }
      return TypedFunctionCall.builder()
          .args(typedArgs)
          .typedModuleScope(typedModuleScope)
          .functionCallAst(this)
          .callTarget(callTargetName)
          .returnType(declaredReturnType)
          .typeScope(typeScope)
          .build();
    }
  }

  public TypedExpression createInvokeForExpr(TypeScope typeScope, TypedModuleScope typedModuleScope,
      ToTypedFunctionContext compileContext, String callTargetName, List<TypedExpression> typedArgs,
      List<? extends LambdaRodeoType> callArgTypes) {
    TypedExpression variable = callTarget
        .toTypedExpression(typeScope, typedModuleScope, compileContext);
    // Check to see if the variable is a lambda of the correct arity:
    LambdaRodeoType varType = variable.getType();
    if (!(varType instanceof Lambda)) {
      if (!(Objects.equals(varType, Atom.UNDEFINED))) {
        compileContext.getCompileErrorCollector().collect(
            CompileError.triedToCallNonFunction(callTarget, callTargetName)
        );
      }
      return AtomAst.undefinedAtomExpression();
    }
    // Check to make sure the arity of the call lines up
    Lambda asLambda = (Lambda) varType;
    List<? extends LambdaRodeoType> argTypes = asLambda.getArgs();
    if (argTypes.size() != typedArgs.size()) {
      compileContext.getCompileErrorCollector().collect(
          CompileError.calledFunctionWithWrongArgs(this, argTypes, callArgTypes)
      );
      return AtomAst.undefinedAtomExpression();
    }
    // Now check to make sure all the args can be assigned to the arguments:
    if (!checkCallMatchesSignature(compileContext, callArgTypes, argTypes)) {
      return AtomAst.undefinedAtomExpression();
    }
    // Now we can return a typed invoke!
    return TypedLambdaInvoke.builder()
        .args(typedArgs)
        .typedModuleScope(typedModuleScope)
        .expr(this)
        .type(asLambda.getReturnType())
        .invokeTarget(variable)
        .build();
  }

  public boolean checkCallMatchesSignature(ToTypedFunctionContext compileContext,
      List<? extends LambdaRodeoType> callArgTypes, List<? extends LambdaRodeoType> argTypes) {
    for (int i = 0; i < argTypes.size(); i++) {
      LambdaRodeoType argType = argTypes.get(i);
      LambdaRodeoType calledArg = callArgTypes.get(i);
      if (!argType.assignableFrom(calledArg)) {
        compileContext.getCompileErrorCollector().collect(
            CompileError.calledFunctionWithWrongArgs(this, argTypes, callArgTypes)
        );
        return false;
      }
    }
    return true;
  }

  @Override
  public Set<String> getReferencedVariables() {
    return args.stream()
        .flatMap(arg -> arg.getReferencedVariables().stream())
        .collect(Collectors.toSet());
  }
}
