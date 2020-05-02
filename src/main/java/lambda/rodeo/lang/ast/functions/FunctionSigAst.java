package lambda.rodeo.lang.ast.functions;

import java.util.ArrayList;
import java.util.List;
import lambda.rodeo.lang.AstNode;
import lambda.rodeo.lang.types.TypeScope;
import lambda.rodeo.lang.types.Type;
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
  private final Type declaredReturnType;

  public TypeScope getInitialTypeScope(TypeScope moduleScope) {
    TypeScope typeScope = moduleScope;
    for (TypedVar arg : arguments) {
      typeScope = typeScope.declare(arg.getName(), arg.getType());
    }
    return typeScope;
  }
}
