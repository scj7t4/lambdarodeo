package lambda.rodeo.lang.ast.statements;

import lambda.rodeo.lang.AstNode;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.ast.expressions.ExpressionAst;
import lambda.rodeo.lang.typed.expressions.TypedExpression;
import lambda.rodeo.lang.scope.TypeScope;
import lambda.rodeo.lang.typed.statements.TypedAssignment;
import lambda.rodeo.lang.typed.statements.TypedStatement;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class StatementAst implements AstNode {

  private final ExpressionAst expression;
  private final AssigmentAst assignment;
  private final int startLine;
  private final int endLine;
  private final int characterStart;

  public TypedStatement toTypedStatementAst(TypeScope before, CompileContext compileContext) {
    TypedExpression typedExpr = getExpression().toTypedExpression(before, compileContext);
    TypeScope after = before;
    TypedAssignment typedAssignment = null;
    if (assignment != null) {
      after = assignment.scopeAfter(before, compileContext, typedExpr.getType());
      typedAssignment = assignment.toTypedAssignmentAst(after);
    }

    return TypedStatement.builder()
        .statementAst(this)
        .beforeTypeScope(before)
        .afterTypeScope(after)
        .typedExpression(typedExpr)
        .typedAssignment(typedAssignment)
        .build();
  }
}
