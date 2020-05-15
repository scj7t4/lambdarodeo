package lambda.rodeo.lang.s1ast.functions;

import java.util.List;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.compilation.CompileError;
import lambda.rodeo.lang.s2typed.functions.TypedFunction;
import lambda.rodeo.lang.s2typed.functions.TypedFunctionBody;
import lambda.rodeo.lang.s2typed.functions.patterns.TypedPatternCase;
import lambda.rodeo.lang.scope.TypeScope;
import lambda.rodeo.lang.scope.TypedModuleScope;
import lambda.rodeo.runtime.types.Type;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

/*
 * Function is composed of multiple statements A -> B -> C -> D
 *
 * We would like to determine last function call <- | -> everything
 * to the right is grouped into a result call..
 */
@Data
@Builder
@EqualsAndHashCode
public class FunctionAst {

  @NonNull
  private final FunctionSigAst functionSignature;
  @NonNull
  private final FunctionBodyAst functionBodyAst;
  private final boolean lambda;

  public TypedFunction toTypedFunctionAst(
      TypeScope moduleScope,
      TypedModuleScope typedModuleScope,
      CompileContext compileContext) {

    ToTypedFunctionContext toTypedFunctionContext = ToTypedFunctionContext.builder()
        .compileContext(compileContext)
        .functionName(functionSignature.getName())
        .build();

    TypedFunctionBody typedFunctionBody = functionBodyAst.toTypedFunctionBodyAst(
        functionSignature.getInitialTypeScope(moduleScope),
        typedModuleScope,
        toTypedFunctionContext);

    List<TypedPatternCase> patternCases = typedFunctionBody.getPatternCases();
    for (TypedPatternCase typedPatternCase : patternCases) {
      Type returnedType = typedPatternCase.getReturnedType();
      Type declaredReturnedType = functionSignature.getDeclaredReturnType();

      if (!declaredReturnedType.assignableFrom(returnedType)) {
        compileContext.getCompileErrorCollector().collect(
            CompileError
                .returnTypeMismatch(typedPatternCase.getPatternCaseAst(), declaredReturnedType,
                    returnedType)
        );
      }
    }

    return TypedFunction.builder()
        .functionAst(this)
        .functionBody(typedFunctionBody)
        .functionSigAst(functionSignature)
        .build();
  }

  public String getName() {
    return getFunctionSignature().getName();
  }

  public List<TypedVar> getArguments() {
    return getFunctionSignature().getArguments();
  }

  public boolean hasSignature(List<Type> callArguments) {
    List<TypedVar> sigArguments = functionSignature.getArguments();
    if (callArguments.size() != sigArguments.size()) {
      return false;
    }

    for (int i = 0; i < callArguments.size(); i++) {
      Type sigArg = sigArguments.get(0).getType();
      if (!sigArg.assignableFrom(callArguments.get(0))) {
        return false;
      }
    }
    return true;
  }
}
