package lambda.rodeo.lang.statements;

import lambda.rodeo.lang.antlr.LambdaRodeoBaseListener;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.AssignmentContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.StatementContext;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.exceptions.CriticalLanguageException;
import lambda.rodeo.lang.expressions.ExpressionAst;
import lambda.rodeo.lang.expressions.ExpressionAstFactory;
import lambda.rodeo.lang.types.Type;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class StatementAstFactory extends LambdaRodeoBaseListener {

  private StatementAst.StatementAstBuilder builder = StatementAst.builder();
  private final TypeScope typeScope;
  private final CompileContext compileContext;

  public StatementAstFactory(
      StatementContext ctx,
      TypeScope typeScope,
      CompileContext compileContext) {
    this.compileContext = compileContext;
    this.typeScope = typeScope;
    builder = builder.scopeBefore(typeScope);
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

    ExpressionAstFactory expressionAstFactory = new ExpressionAstFactory(ctx.expr(),
        typeScope,
        compileContext);
    ExpressionAst expressionAst = expressionAstFactory.toAst();

    AssigmentAst assignmentAst = null;
    TypeScope scopeAfter = typeScope;
    if (simpleAssignmentAst != null) {
      Type type = expressionAst.getType();
      scopeAfter = simpleAssignmentAst.scopeAfter(typeScope, type);
      String identifier = simpleAssignmentAst.getIdentifier();
      int index = scopeAfter.get(identifier)
          .orElseThrow(() -> new CriticalLanguageException(
              "Assignment of '" + identifier + "'doesn't exist in scope"))
          .getIndex();
      assignmentAst = AssigmentAst.builder()
          .identifier(simpleAssignmentAst.getIdentifier())
          .type(type)
          .index(index)
          .build();
    }

    builder = builder
        .assignment(assignmentAst)
        .scopeAfter(scopeAfter)
        .expression(expressionAst);
  }
}
