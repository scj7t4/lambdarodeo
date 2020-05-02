package lambda.rodeo.lang.ast.statements;

import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.ast.expressions.ExpressionAst;
import lambda.rodeo.lang.typed.expressions.TypedExpressionAst;
import lambda.rodeo.lang.types.TypeScope;
import lambda.rodeo.lang.typed.statements.TypedAssignmentAst;
import lambda.rodeo.lang.typed.statements.TypedStatementAst;
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
