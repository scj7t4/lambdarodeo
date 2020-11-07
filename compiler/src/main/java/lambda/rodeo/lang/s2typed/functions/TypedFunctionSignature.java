package lambda.rodeo.lang.s2typed.functions;

import java.util.ArrayList;
import java.util.List;
import lambda.rodeo.lang.compilation.S2CompileContext;
import lambda.rodeo.lang.s1ast.functions.FunctionSigAst;
import lambda.rodeo.lang.s2typed.type.S2TypedVar;
import lambda.rodeo.lang.scope.TypeScope;
import lambda.rodeo.lang.scope.TypedModuleScope;
import lambda.rodeo.lang.types.CompileableType;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
public class TypedFunctionSignature {
  @NonNull
  private final FunctionSigAst from;
  @NonNull
  @Builder.Default
  private final List<S2TypedVar> arguments = new ArrayList<>();
  @NonNull
  private final CompileableType declaredReturnType;

  public String getName() {
    return getFrom().getName();
  }

  public int getStartLine() {
    return getFrom().getStartLine();
  }

  public int getEndLine() {
    return getFrom().getEndLine();
  }

  public int getCharacterStart() {
    return getFrom().getCharacterStart();
  }

  public TypeScope getInitialTypeScope(
      TypeScope moduleScope,
      TypedModuleScope typedModuleScope,
      S2CompileContext compileContext) {
    return getFrom().getInitialTypeScope(moduleScope, typedModuleScope, compileContext);
  }

  public void checkCollisionAgainstModule(TypedModuleScope typedModuleScope,
      S2CompileContext compileContext) {
    getFrom().checkCollisionAgainstModule(typedModuleScope, compileContext);
  }
}
