package lambda.rodeo.lang.s2typed.expressions;

import java.util.List;
import java.util.stream.Collectors;
import lambda.rodeo.lang.s1ast.expressions.ExpressionAst;
import lambda.rodeo.lang.s1ast.expressions.ObjectAst;
import lambda.rodeo.lang.s2typed.types.AnonymousTypedInterface;
import lambda.rodeo.lang.s2typed.types.S2AnonymousTypedVar;
import lambda.rodeo.lang.s2typed.types.TypedInterface;
import lambda.rodeo.lang.s3compileable.expression.CompileableExpr;
import lambda.rodeo.lang.types.CompileableType;
import lambda.rodeo.runtime.types.LRInterface;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
public class TypedObject implements TypedExpression {

  @Builder
  @Getter
  public static class TypedObjectEntry {

    @NonNull
    private final String identifier;
    @NonNull
    private final TypedExpression expression;
  }

  @NonNull
  private final ObjectAst objectAst;

  @NonNull
  private final List<TypedObjectEntry> entries;

  @Override
  public CompileableType getType() {
    AnonymousTypedInterface.builder()
        .members(entries.stream()
            .map(entry -> S2AnonymousTypedVar.builder()
                .type(entry.getExpression().getType())
                .name(entry.getIdentifier())
                .build())
            .collect(Collectors.toList()))
        .build();
    return null;
  }

  @Override
  public ExpressionAst getExpr() {
    return null;
  }

  @Override
  public CompileableExpr toCompileableExpr() {
    return null;
  }
}
