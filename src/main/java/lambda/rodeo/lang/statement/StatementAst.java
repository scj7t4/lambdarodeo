package lambda.rodeo.lang.statement;

import lambda.rodeo.lang.expressions.ExpressionAst;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class StatementAst {
  private final ExpressionAst expression;
  private final SimpleAssignmentAst assignment;

  Scope compute(Scope scope) {
    Object computed = expression.getComputable().compute(scope);
    TypedValue value = TypedValue.builder()
        .type(expression.getType())
        .value(computed)
        .build();
    Scope result = scope.put("$last", value);
    if(assignment != null) {
      return assignment.assign(result, value);
    }
    return result;
  }
}
