package lambda.rodeo.lang.s2typed.functions;

import java.util.List;
import lambda.rodeo.lang.s1ast.functions.FunctionAst;
import lambda.rodeo.lang.s1ast.functions.FunctionSigAst;
import lambda.rodeo.lang.s1ast.functions.TypedVar;
import lambda.rodeo.lang.s3compileable.functions.CompileableFunction;
import lambda.rodeo.lang.scope.CompileableModuleScope;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class TypedFunction {
  private final FunctionAst functionAst;
  private final TypedFunctionBody typedFunctionBody;
  private final FunctionSigAst functionSigAst;

  public CompileableFunction toCompileableFunction(
      CompileableModuleScope compileableModuleScope) {
    return CompileableFunction.builder()
        .functionBodyAst(typedFunctionBody.toCompileableFunctionBody(compileableModuleScope))
        .functionSigAst(functionSigAst)
        .typedFunction(this)
        .build();
  }

  public String getName() {
    return getFunctionSigAst().getName();
  }

  public List<TypedVar> getArguments() {
    return getFunctionSigAst().getArguments();
  }
}
