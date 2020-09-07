package lambda.rodeo.lang.s2typed.functions;

import java.util.List;
import java.util.Map;
import lambda.rodeo.lang.s1ast.functions.FunctionAst;
import lambda.rodeo.lang.s1ast.functions.FunctionSigAst;
import lambda.rodeo.lang.s1ast.type.TypedVar;
import lambda.rodeo.lang.s2typed.functions.patterns.TypedCaseArg;
import lambda.rodeo.lang.s3compileable.functions.CompileableFunction;
import lambda.rodeo.lang.s2typed.functions.patterns.TypedStaticPattern;
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
      Map<TypedCaseArg, TypedStaticPattern> staticPatterns) {
    return CompileableFunction.builder()
        .functionBody(functionBody.toCompileableFunctionBody(staticPatterns))
        .functionSignature(functionSigAst.toTypedFunctionSignature())
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
