package lambda.rodeo.lang.statements;

import lambda.rodeo.lang.antlr.LambdaRodeoBaseListener;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.AssignmentContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.StatementContext;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.expressions.ExpressionAst;
import lambda.rodeo.lang.expressions.ExpressionAstFactory;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class StatementAstFactory extends LambdaRodeoBaseListener {

  private StatementAst.StatementAstBuilder builder = StatementAst.builder();
  private final TypeScope typeScope;
  private final CompileContext compileContext;

  public StatementAstFactory(StatementContext ctx, TypeScope typeScope,
      CompileContext compileContext) {
    this.compileContext = compileContext;
    this.typeScope = typeScope;
    ParseTreeWalker.DEFAULT.walk(this, ctx);
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

    ExpressionAstFactory expressionAstFactory = new ExpressionAstFactory(ctx.expr(),
        typeScope,
        compileContext);
    ExpressionAst expressionAst = expressionAstFactory.toAst();

    builder = builder
        .assignment(simpleAssignmentAst)
        .expression(expressionAst);
  }
}
