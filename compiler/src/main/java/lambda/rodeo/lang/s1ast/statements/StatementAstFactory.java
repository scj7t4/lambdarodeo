package lambda.rodeo.lang.s1ast.statements;

import lambda.rodeo.lang.antlr.LambdaRodeoParser.AssignmentContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.ExprContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.LambdaStatementContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.StatementContext;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.s1ast.expressions.ExpressionAst;
import lambda.rodeo.lang.s1ast.expressions.ExpressionAstFactory;
import org.antlr.v4.runtime.ParserRuleContext;

public class StatementAstFactory {

  private final CompileContext compileContext;
  private final StatementAst statementAst;

  public StatementAstFactory(CompileContext compileContext,
      StatementContext ctx) {
    this.compileContext = compileContext;
    statementAst = handleStatement(ctx, ctx.assignment(), ctx.expr(), compileContext);
  }

  public StatementAstFactory(CompileContext compileContext,
      LambdaStatementContext ctx) {
    this.compileContext = compileContext;
    statementAst = handleStatement(ctx, ctx.assignment(), ctx.expr(), compileContext);
  }

  public StatementAst toAst() {
    return statementAst;
  }

  public static StatementAst handleStatement(ParserRuleContext where,
      AssignmentContext assignment,
      ExprContext exprContext,
      CompileContext compileContext) {
    SimpleAssignmentAst simpleAssignmentAst = null;

    if (assignment != null) {
      AssignmentAstFactory assignmentAstFactory = new AssignmentAstFactory(assignment);
      simpleAssignmentAst = assignmentAstFactory.toAst();
    }

    ExpressionAstFactory expressionAstFactory = new ExpressionAstFactory(exprContext,
        compileContext);
    ExpressionAst expressionAst = expressionAstFactory.toAst();

    return StatementAst.builder()
        .assignment(simpleAssignmentAst)
        .expression(expressionAst)
        .startLine(where.getStart().getLine())
        .endLine(where.getStop().getLine())
        .characterStart(where.getStart().getCharPositionInLine())
        .build();
  }
}
