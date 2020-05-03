package lambda.rodeo.lang.s1ast.statements;

import lambda.rodeo.lang.antlr.LambdaRodeoBaseListener;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.AssignmentContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.StatementContext;
import lambda.rodeo.lang.s1ast.expressions.ExpressionAst;
import lambda.rodeo.lang.s1ast.expressions.ExpressionAstFactory;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class StatementAstFactory extends LambdaRodeoBaseListener {

  private StatementAst.StatementAstBuilder builder = StatementAst.builder();

  public StatementAstFactory(
      StatementContext ctx) {
    ParseTreeWalker.DEFAULT.walk(this, ctx);
  }

  public StatementAst toAst() {
    return builder.build();
  }

  @Override
  public void enterStatement(StatementContext ctx) {
    AssignmentContext assignment = ctx.assignment();
    SimpleAssignmentAst simpleAssignmentAst = null;

    if (assignment != null) {
      AssignmentAstFactory assignmentAstFactory = new AssignmentAstFactory(assignment);
      simpleAssignmentAst = assignmentAstFactory.toAst();
    }

    ExpressionAstFactory expressionAstFactory = new ExpressionAstFactory(ctx.expr());
    ExpressionAst expressionAst = expressionAstFactory.toAst();

    builder = builder
        .assignment(simpleAssignmentAst)
        .expression(expressionAst)
        .startLine(ctx.getStart().getLine())
        .endLine(ctx.getStop().getLine())
        .characterStart(ctx.getStart().getCharPositionInLine());
  }
}
