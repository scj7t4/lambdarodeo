package lambda.rodeo.lang.s1ast.statements;

import lambda.rodeo.lang.AstNode;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.s1ast.expressions.ExpressionAst;
import lambda.rodeo.lang.s2typed.expressions.TypedExpression;
import lambda.rodeo.lang.scope.TypeScope;
import lambda.rodeo.lang.s2typed.statements.TypedAssignment;
import lambda.rodeo.lang.s2typed.statements.TypedStatement;
import lambda.rodeo.lang.scope.TypedModuleScope;
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

  public TypedStatement toTypedStatementAst(
      TypeScope before,
      TypedModuleScope typedModuleScope,
      CompileContext compileContext) {
    TypedExpression typedExpr = getExpression()
        .toTypedExpression(before, typedModuleScope, compileContext);
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
