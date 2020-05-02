package lambda.rodeo.lang.ast.functions;

import java.util.List;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.typed.functions.TypedFunction;
import lambda.rodeo.lang.types.TypeScope;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/*
 * Function is composed of multiple statements A -> B -> C -> D
 *
 * We would like to determine last function call <- | -> everything
 * to the right is grouped into a result call..
 */
@Data
@Builder
@EqualsAndHashCode
//TODO: Compile error when types don't agree
public class FunctionAst {

  private final FunctionSigAst functionSignature;
  private final FunctionBodyAst functionBodyAst;

  public TypedFunction toTypedFunctionAst(TypeScope moduleScope, CompileContext compileContext) {
    return TypedFunction.builder()
        .functionAst(this)
        .typedFunctionBody(
            functionBodyAst.toTypedFunctionBodyAst(
            functionSignature.getInitialTypeScope(moduleScope),
            compileContext))
        .functionSigAst(functionSignature)
        .build();
  }

  public String getName() {
    return getFunctionSignature().getName();
  }

  public List<TypedVar> getArguments() {
    return getFunctionSignature().getArguments();
  }
}
