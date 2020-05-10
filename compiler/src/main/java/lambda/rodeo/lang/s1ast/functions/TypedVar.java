package lambda.rodeo.lang.s1ast.functions;

import lambda.rodeo.lang.AstNode;
import lambda.rodeo.runtime.types.Type;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode
public class TypedVar implements AstNode {

  private final String name;
  private final Type type;
  private final int startLine;
  private final int endLine;
  private final int characterStart;
}
