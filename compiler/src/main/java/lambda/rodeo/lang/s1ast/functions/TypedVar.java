package lambda.rodeo.lang.s1ast.functions;

import lambda.rodeo.lang.AstNode;
import lambda.rodeo.runtime.types.LambdaRodeoType;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode
public class TypedVar implements AstNode {

  private final String name;
  private final LambdaRodeoType type;
  private final int startLine;
  private final int endLine;
  private final int characterStart;
}
