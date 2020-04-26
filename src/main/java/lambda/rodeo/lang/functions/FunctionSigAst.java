package lambda.rodeo.lang.functions;

import java.util.ArrayList;
import java.util.List;
import lambda.rodeo.lang.AstNode;
import lambda.rodeo.lang.statements.TypeScope;
import lambda.rodeo.lang.types.Type;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FunctionSigAst implements AstNode {
  private final String name;
  @Builder.Default
  private final List<TypedVarAst> arguments = new ArrayList<>();
  private final int startLine;
  private final int endLine;
  private final int characterStart;
  private final Type declaredReturnType;

  public TypeScope getInitialTypeScope() {
    TypeScope typeScope = new TypeScope();
    for (TypedVarAst arg : arguments) {
      typeScope = typeScope.declare(arg.getName(), arg.getType());
    }
    return typeScope;
  }
}
