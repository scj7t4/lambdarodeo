package lambda.rodeo.lang.s2typed.functions;

import java.util.List;
import java.util.Map;
import lambda.rodeo.lang.compilation.CollectsErrors;
import lambda.rodeo.lang.s1ast.functions.FunctionAst;
import lambda.rodeo.lang.s1ast.functions.FunctionSigAst;
import lambda.rodeo.lang.s1ast.type.TypedVar;
import lambda.rodeo.lang.s2typed.functions.patterns.TypedCaseArg;
import lambda.rodeo.lang.s2typed.functions.patterns.TypedStaticPattern;
import lambda.rodeo.lang.s3compileable.functions.CompileableFunction;
import lambda.rodeo.lang.s3compileable.functions.CompileableFunctionBody;
import lambda.rodeo.lang.scope.TypedModuleScope;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class TypedFunction {

  private final FunctionAst functionAst;
  private final TypedFunctionBody functionBody;
  private final FunctionSigAst functionSigAst;

  public CompileableFunction toCompileableFunction(
      Map<TypedCaseArg, TypedStaticPattern> staticPatterns,
      TypedModuleScope typedModuleScope,
      CollectsErrors compileContext) {
    TypedFunctionSignature functionSignature = functionSigAst.toTypedFunctionSignature(
        typedModuleScope,
        compileContext);

    CompileableFunctionBody functionBody = this.functionBody
        .toCompileableFunctionBody(staticPatterns, typedModuleScope, compileContext);

    return CompileableFunction.builder()
        .functionBody(functionBody)
        .functionSignature(
            functionSignature)
        .typedFunction(this)
        .build();
  }

  public String getName() {
    return getFunctionSigAst().getName();
  }

  public List<TypedVar> getArguments() {
    return getFunctionSigAst().getArguments();
  }

  public boolean isLambda() {
    return getFunctionAst().isLambda();
  }
}
