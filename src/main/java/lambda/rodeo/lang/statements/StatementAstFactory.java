package lambda.rodeo.lang.statements;

import lambda.rodeo.lang.antlr.LambdaRodeoBaseListener;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.AssignmentContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.StatementContext;
import lambda.rodeo.lang.expressions.ExpressionAst;
import lambda.rodeo.lang.expressions.ExpressionAstFactory;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class StatementAstFactory extends LambdaRodeoBaseListener {

  private StatementAst.StatementAstBuilder builder = StatementAst.builder();
  private final TypeScope typeScope;

  public StatementAstFactory(StatementContext ctx, TypeScope typeScope) {
    ParseTreeWalker.DEFAULT.walk(this, ctx);
    this.typeScope = typeScope;
  }

  public StatementAst toAst() {
    return builder.build();
  }

  @Override
  public void enterStatement(StatementContext ctx) {
    AssignmentContext assignment = ctx.assignment();
    SimpleAssignmentAst simpleAssignmentAst = null;

    if(assignment != null) {
      AssignmentAstFactory assignmentAstFactory = new AssignmentAstFactory(assignment);
      simpleAssignmentAst = assignmentAstFactory.toAst();
    }

    ExpressionAstFactory expressionAstFactory = new ExpressionAstFactory(ctx.expr(), typeScope);
    ExpressionAst expressionAst = expressionAstFactory.toAst();

    builder = builder
        .assignment(simpleAssignmentAst)
        .expression(expressionAst);
  }
}
