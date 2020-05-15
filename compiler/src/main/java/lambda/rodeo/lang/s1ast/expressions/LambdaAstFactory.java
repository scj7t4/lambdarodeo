package lambda.rodeo.lang.s1ast.expressions;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.LambdaContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.LambdaExprContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.LambdaStatementContext;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.s1ast.functions.TypedVar;
import lambda.rodeo.lang.s1ast.functions.TypedVarFactory;
import lambda.rodeo.lang.s1ast.statements.StatementAst;
import lambda.rodeo.lang.s1ast.statements.StatementAstFactory;

public class LambdaAstFactory {

  private final List<TypedVar> args;
  private final List<StatementAst> statements;

  public LambdaAstFactory(LambdaContext ctx,
      CompileContext compileContext) {
    args = ctx.lambdaTypedVar()
        .stream()
        .map(varContext -> new TypedVarFactory(varContext).toAst())
        .collect(Collectors.toList());

    List<LambdaStatementContext> lambdaStatementContexts = ctx.lambdaStatement();
    if(lambdaStatementContexts == null || lambdaStatementContexts.isEmpty()) {
      LambdaExprContext lambdaExpr = ctx.lambdaExpr();
      ExpressionAst expressionAst = new ExpressionAstFactory(
          lambdaExpr.expr(), compileContext).toAst();
      statements = Collections.singletonList(StatementAst.builder()
          .endLine(ctx.getStop().getLine())
          .startLine(ctx.getStart().getLine())
          .characterStart(ctx.getStart().getCharPositionInLine())
          .expression(expressionAst)
          .build());
    } else {
      statements = lambdaStatementContexts.stream()
          .map(statementCtx -> new StatementAstFactory(compileContext, statementCtx).toAst())
          .collect(Collectors.toList());
    }
  }

  LambdaAst toAst() {
    return LambdaAst.builder()
        .arguments(args)
        .statements(statements)
        .build();
  }

}
