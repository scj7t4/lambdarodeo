package lambda.rodeo.lang.s1ast.expressions;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lambda.rodeo.lang.s1ast.functions.ToTypedFunctionContext;
import lambda.rodeo.lang.s2typed.expressions.TypedExpression;
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
  public static class ObjectAstEntry {
    @NonNull
    private final String identifier;
    @NonNull
    private final ExpressionAst expression;
  }

  private final List<ObjectAstEntry> objectAstEntries;
  private final int startLine;
  private final int endLine;
  private final int characterStart;

  @Override
  public TypedExpression toTypedExpression(TypeScope scope, TypedModuleScope typedModuleScope,
      ToTypedFunctionContext compileContext) {
    //TODO: Pick up here and make typed expression for our object!
    return null;
  }

  @Override
  public Set<String> getReferencedVariables() {
    return objectAstEntries.stream()
        .flatMap(x -> x.getExpression().getReferencedVariables().stream())
        .collect(Collectors.toSet());
  }

}
