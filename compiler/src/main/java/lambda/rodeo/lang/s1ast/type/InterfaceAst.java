package lambda.rodeo.lang.s1ast.type;

import java.util.List;
import lambda.rodeo.lang.AstNode;
import lambda.rodeo.lang.s1ast.functions.TypedVar;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
public class InterfaceAst implements AstNode {
  private final int startLine;
  private final int endLine;
  private final int characterStart;

  @NonNull
  private final String name;

  @NonNull
  private final List<TypedVar> members;
}
