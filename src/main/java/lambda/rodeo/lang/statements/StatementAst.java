package lambda.rodeo.lang.statements;

import lambda.rodeo.lang.expressions.ExpressionAst;
import lambda.rodeo.lang.types.Type;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class StatementAst {
  private final ExpressionAst expression;
  private final SimpleAssignmentAst assignment;

  public Scope compute(Scope scope) {
    Object computed = expression.getComputable().compute(scope);
    Scope result = scope.put("$last", computed);
    if(assignment != null) {
      return assignment.assign(result, computed);
    }
    return result;
  }

  public TypeScope typeScope(TypeScope typeScope) {
    Type type = getType(typeScope);
    TypeScope result = typeScope.put("$last", type);
    if(assignment != null) {
      return assignment.type(result, type);
    }
    return result;
  }

  public Type getType(TypeScope typeScope) {
    return getExpression().getType(typeScope);
  }
}
