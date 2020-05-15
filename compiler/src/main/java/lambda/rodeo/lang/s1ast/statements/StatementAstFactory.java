package lambda.rodeo.lang.s1ast.statements;

import lambda.rodeo.lang.antlr.LambdaRodeoBaseListener;
import lambda.rodeo.lang.antlr.LambdaRodeoBaseVisitor;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.AssignmentContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.ExprContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.LambdaStatementContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.StatementContext;
import lambda.rodeo.lang.s1ast.expressions.ExpressionAst;
import lambda.rodeo.lang.s1ast.expressions.ExpressionAstFactory;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class StatementAstFactory {

  private final StatementAst statementAst;

  public StatementAstFactory(StatementContext ctx) {
    statementAst = handleStatement(ctx, ctx.assignment(), ctx.expr());
  }

  public StatementAstFactory(LambdaStatementContext ctx) {
    statementAst = handleStatement(ctx, ctx.assignment(), ctx.expr());
  }

  public StatementAst toAst() {
    return statementAst;
  }

  public static StatementAst handleStatement(ParserRuleContext where,
      AssignmentContext assignment,
      ExprContext exprContext) {
    SimpleAssignmentAst simpleAssignmentAst = null;

    if (assignment != null) {
      AssignmentAstFactory assignmentAstFactory = new AssignmentAstFactory(assignment);
      simpleAssignmentAst = assignmentAstFactory.toAst();
    }

    ExpressionAstFactory expressionAstFactory = new ExpressionAstFactory(exprContext);
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
