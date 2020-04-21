package lambda.rodeo.lang.statement;

import lambda.rodeo.lang.antlr.LambdaRodeoBaseListener;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.AssignmentContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.StatementContext;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class StatementAstFactory extends LambdaRodeoBaseListener {

  public StatementAstFactory(StatementContext ctx) {
    ParseTreeWalker.DEFAULT.walk(this, ctx);
  }

  @Override
  public void enterAssignment(AssignmentContext ctx) {
    super.enterAssignment(ctx);
  }
}
