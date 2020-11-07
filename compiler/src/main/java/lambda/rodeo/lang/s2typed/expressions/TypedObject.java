package lambda.rodeo.lang.s2typed.expressions;

import java.util.List;
import java.util.stream.Collectors;
import lambda.rodeo.lang.compilation.CollectsErrors;
import lambda.rodeo.lang.s1ast.expressions.ObjectAst;
import lambda.rodeo.lang.s1ast.type.TypedVar;
import lambda.rodeo.lang.s2typed.type.S2TypedVar;
import lambda.rodeo.lang.s2typed.type.SourcedTypedVar;
import lambda.rodeo.lang.s3compileable.expression.CompileableExpr;
import lambda.rodeo.lang.types.CompileableType;
import lambda.rodeo.lang.types.LRInterface;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
public class TypedObject implements TypedExpression {

  @Builder
  @Getter
  public static class TypedObjectMember {
    @NonNull
    private final String identifier;
    @NonNull
    private final TypedExpression expression;
  }

  @NonNull
  private final ObjectAst from;
  @NonNull
  private final List<TypedObjectMember> members;

  @Override
  public CompileableType getType() {
    List<S2TypedVar> members = this.members.stream()
        .map(entry -> SourcedTypedVar.builder()
            .from(TypedVar.builder()
                .type(entry.getExpression().getType().getType())
                .name(entry.getIdentifier())
                .build())
            .type(entry.getExpression().getType())
            .build())
        .collect(Collectors.toList());
    return LRInterface.builder()
        .members(members)
        .build();
  }

  @Override
  public ObjectAst getExpr() {
    return from;
  }

  @Override
  public CompileableExpr toCompileableExpr(
      CollectsErrors compileContext) {
    return null;
  }
}
