package lambda.rodeo.lang.statement;

import lambda.rodeo.lang.expressions.ExpressionAst;
import lambda.rodeo.lang.values.Computable;
import lambda.rodeo.lang.values.Variable;
import lombok.Builder;

@Builder
public class StatementAst {
  private final ExpressionAst expression;
  private final AssignmentAst assignment;

  Scope compute(Scope scope) {
    Object computed = expression.getComputable().compute(scope);
    Scope result = scope.put("$last", TypedValue.builder()
        .type(expression.getType())
        .value(computed)
        .build());
    if(assignment != null) {
      return assignment.assign(result, expression.getType(), computed);
    }
    return result;
  }
}
