package lambda.rodeo.lang.functions;

import java.util.ArrayList;
import java.util.List;
import lambda.rodeo.lang.AstNode;
import lambda.rodeo.lang.statements.TypeScope;
import lombok.Builder;
import lombok.Getter;
import org.objectweb.asm.Type;

@Builder
@Getter
public class FunctionSigAst implements AstNode {
  private final String name;
  @Builder.Default
  private final List<TypedVarAst> arguments = new ArrayList<>();
  private final int startLine;
  private final int endLine;
  private final int characterStart;

  public TypeScope getInitialTypeScope() {
    TypeScope typeScope = new TypeScope();
    for (TypedVarAst arg : arguments) {
      typeScope = typeScope.declare(arg.getName(), arg.getType());
    }
    return typeScope;
  }

  public String generateFunctionDescriptor() {
    StringBuilder sb = new StringBuilder();
    sb.append("(");
    for (TypedVarAst var : arguments) {
      String descriptor = Type.getDescriptor(var.getType().javaType());
      sb.append(descriptor);
    }
    sb.append(")").append(Type.getDescriptor(Result.class));
    return sb.toString();
  }
}
