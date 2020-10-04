package lambda.rodeo.lang.s1ast.type;

import lambda.rodeo.lang.AstNode;
import lambda.rodeo.lang.s2typed.types.S2SourcedTypedVar;
import lambda.rodeo.lang.s2typed.types.S2TypedVar;
import lambda.rodeo.lang.types.LambdaRodeoType;
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

  public S2TypedVar toS2TypedVar() {
    return S2SourcedTypedVar.builder()
        .from(this)
        .type(type.toCompileableType())
        .build();
  }
}
