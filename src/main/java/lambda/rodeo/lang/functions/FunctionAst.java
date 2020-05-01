package lambda.rodeo.lang.functions;

import java.util.List;
import lambda.rodeo.lang.compilation.CompileContext;
import lombok.Builder;
import lombok.Data;

/*
 * Function is composed of multiple statements A -> B -> C -> D
 *
 * We would like to determine last function call <- | -> everything
 * to the right is grouped into a result call..
 */
@Data
@Builder
//TODO: Compile error when types don't agree
public class FunctionAst {

  private final FunctionSigAst functionSignature;
  private final FunctionBodyAst functionBodyAst;

  public TypedFunctionAst toTypedFunctionAst(CompileContext compileContext) {
    return TypedFunctionAst.builder()
        .functionAst(this)
        .functionBodyAst(functionBodyAst.toTypedFunctionBodyAst(
            functionSignature.getInitialTypeScope(),
            compileContext))
        .functionSigAst(functionSignature)
        .build();
  }

  public String getName() {
    return getFunctionSignature().getName();
  }

  public List<TypedVarAst> getArguments() {
    return getFunctionSignature().getArguments();
  }
}
