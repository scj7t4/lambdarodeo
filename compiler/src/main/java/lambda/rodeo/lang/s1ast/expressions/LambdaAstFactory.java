package lambda.rodeo.lang.s1ast.expressions;

import java.util.ArrayList;
import java.util.List;
import lambda.rodeo.lang.antlr.LambdaRodeoBaseListener;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.LambdaContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.LambdaExprContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.LambdaStatementContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.LambdaTypedVarContext;
import lambda.rodeo.lang.s1ast.functions.TypedVar;
import lambda.rodeo.lang.s1ast.functions.TypedVarFactory;
import lambda.rodeo.lang.s1ast.statements.StatementAst;
import lambda.rodeo.lang.s1ast.statements.StatementAstFactory;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class LambdaAstFactory extends LambdaRodeoBaseListener {

  private final List<TypedVar> args = new ArrayList<>();
  private final List<StatementAst> statementAsts = new ArrayList<>();

  public LambdaAstFactory(LambdaContext ctx) {
    ParseTreeWalker.DEFAULT.walk(this, ctx);
  }

  @Override
  public void enterLambdaStatement(LambdaStatementContext ctx) {
    statementAsts.add(new StatementAstFactory(ctx).toAst());
  }

  @Override
  public void enterLambdaTypedVar(LambdaTypedVarContext ctx) {
    args.add(new TypedVarFactory(ctx).toAst());
  }

  @Override
  public void enterLambdaExpr(LambdaExprContext ctx) {
    ExpressionAst expressionAst = new ExpressionAstFactory(ctx.expr()).toAst();
    statementAsts.add(StatementAst.builder()
        .endLine(ctx.getStop().getLine())
        .startLine(ctx.getStart().getLine())
        .characterStart(ctx.getStart().getCharPositionInLine())
        .expression(expressionAst)
        .build());
  }

  LambdaAst toAst() {
    return LambdaAst.builder()
        .arguments(args)
        .statements(statementAsts)
        .build();
  }

}
