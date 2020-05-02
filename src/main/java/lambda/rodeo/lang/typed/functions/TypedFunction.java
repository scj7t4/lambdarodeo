package lambda.rodeo.lang.typed.functions;

import java.util.List;
import lambda.rodeo.lang.ast.functions.FunctionAst;
import lambda.rodeo.lang.ast.functions.FunctionSigAst;
import lambda.rodeo.lang.ast.functions.TypedVar;
import lambda.rodeo.lang.compileable.functions.CompileableFunction;
import lambda.rodeo.lang.typed.TypedModule;
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
      List<TypedModule> modules) {
    return CompileableFunction.builder()
        .functionBodyAst(typedFunctionBody.toCompileableFunctionBody(modules))
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
