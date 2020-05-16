package lambda.rodeo.lang.s1ast.functions;

import java.util.ArrayList;
import java.util.List;
import lambda.rodeo.lang.AstNode;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.compilation.CompileError;
import lambda.rodeo.lang.scope.TypeScope;
import lambda.rodeo.lang.scope.TypedModuleScope;
import lambda.rodeo.runtime.types.LambdaRodeoType;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class FunctionSigAst implements AstNode {
  private final String name;
  @Builder.Default
  private final List<TypedVar> arguments = new ArrayList<>();
  private final int startLine;
  private final int endLine;
  private final int characterStart;
  private final LambdaRodeoType declaredReturnType;

  public TypeScope getInitialTypeScope(TypeScope moduleScope) {
    TypeScope typeScope = moduleScope;
    for (TypedVar arg : arguments) {
      typeScope = typeScope.declare(arg.getName(), arg.getType());
    }
    return typeScope;
  }

  public void checkCollisionAgainstModule(TypedModuleScope typedModuleScope,
      CompileContext compileContext) {
    for(TypedVar arg : arguments) {
      if(typedModuleScope.nameExists(arg.getName())) {
        compileContext.getCompileErrorCollector().collect(
            CompileError.identifierAlreadyDeclaredInScope(arg, arg.getName())
        );
      }
    }
  }
}
