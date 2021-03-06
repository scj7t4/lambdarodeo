package lambda.rodeo.lang.s1ast.statements;

import lambda.rodeo.lang.antlr.LambdaRodeoBaseListener;
import lambda.rodeo.lang.antlr.LambdaRodeoParser;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class AssignmentAstFactory extends LambdaRodeoBaseListener {

  private SimpleAssignmentAst simpleAssignmentAst;

  public AssignmentAstFactory(LambdaRodeoParser.AssignmentContext ctx) {
    ParseTreeWalker.DEFAULT.walk(this, ctx);
  }

  public SimpleAssignmentAst toAst() {
    return simpleAssignmentAst;
  }

  @Override
  public void enterAssignment(LambdaRodeoParser.AssignmentContext ctx) {
    String assignTo = ctx.IDENTIFIER().getText();
    simpleAssignmentAst = SimpleAssignmentAst.builder()
        .identifier(assignTo)
        .startLine(ctx.getStart().getLine())
        .endLine(ctx.getStop().getLine())
        .characterStart(ctx.getStart().getCharPositionInLine())
        .build();
  }
}
