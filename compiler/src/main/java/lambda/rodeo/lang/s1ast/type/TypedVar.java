package lambda.rodeo.lang.s1ast.type;

import lambda.rodeo.lang.AstNode;
import lambda.rodeo.lang.compilation.CollectsErrors;
import lambda.rodeo.lang.s2typed.type.S2TypedVar;
import lambda.rodeo.lang.s2typed.type.SourcedTypedVar;
import lambda.rodeo.lang.scope.TypeResolver;
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

  @EqualsAndHashCode.Exclude
  private final int startLine;
  @EqualsAndHashCode.Exclude
  private final int endLine;
  @EqualsAndHashCode.Exclude
  private final int characterStart;

  public S2TypedVar toS2TypedVar(TypeResolver typeResolver,
      CollectsErrors compileContext) {
    return SourcedTypedVar.builder()
        .from(this)
        .type(type.toCompileableType(typeResolver, compileContext))
        .build();
  }
}
