package lambda.rodeo.lang.statements;

import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.expressions.ExpressionAst;
import lambda.rodeo.lang.expressions.TypedExpressionAst;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class StatementAst {

  private final ExpressionAst expression;
  private final AssigmentAst assignment;


  public TypedStatementAst toTypedStatementAst(TypeScope before, CompileContext compileContext) {
    TypedExpressionAst typedExpr = getExpression().toTypedExpressionAst(before, compileContext);
    TypeScope after = before;
    TypedAssignmentAst typedAssignmentAst = null;
    if (assignment != null) {
      after = assignment.scopeAfter(before, compileContext, typedExpr.getType());
      typedAssignmentAst = assignment.toTypedAssignmentAst(after);
    }

    return TypedStatementAst.builder()
        .statementAst(this)
        .beforeTypeScope(before)
        .afterTypeScope(after)
        .typedExpressionAst(typedExpr)
        .typedAssignmentAst(typedAssignmentAst)
        .build();
  }
}
