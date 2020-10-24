package lambda.rodeo.lang.s1ast.expressions;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lambda.rodeo.lang.s1ast.functions.ToTypedFunctionContext;
import lambda.rodeo.lang.s2typed.expressions.TypedExpression;
import lambda.rodeo.lang.s2typed.expressions.TypedObject;
import lambda.rodeo.lang.s2typed.expressions.TypedObject.TypedObjectMember;
import lambda.rodeo.lang.scope.TypeScope;
import lambda.rodeo.lang.scope.TypedModuleScope;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
public class ObjectAst implements ExpressionAst {

  @Builder
  @Getter
  public static class ObjectAstMember {
    @NonNull
    private final String identifier;
    @NonNull
    private final ExpressionAst expression;
  }

  private final List<ObjectAstMember> objectAstMember;
  private final int startLine;
  private final int endLine;
  private final int characterStart;

  @Override
  public TypedExpression toTypedExpression(TypeScope scope, TypedModuleScope typedModuleScope,
      ToTypedFunctionContext compileContext) {

    List<TypedObjectMember> entries = objectAstMember.stream()
        .map(entry -> {
          TypedExpression entryExpr = entry.getExpression()
              .toTypedExpression(scope, typedModuleScope, compileContext);
          return TypedObjectMember.builder()
              .expression(entryExpr)
              .identifier(entry.getIdentifier())
              .build();
        })
        .collect(Collectors.toList());

    return TypedObject.builder()
        .from(this)
        .members(entries)
        .build();
  }

  @Override
  public Set<String> getReferencedVariables() {
    return objectAstMember.stream()
        .flatMap(x -> x.getExpression().getReferencedVariables().stream())
        .collect(Collectors.toSet());
  }

}
