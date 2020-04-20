package lambda.rodeo.lang.expressions;

import java.math.BigInteger;
import java.util.Deque;
import java.util.LinkedList;
import lambda.rodeo.lang.antlr.LambdaRodeoBaseListener;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.AddSubContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.ExprContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.IntLiteralContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.MultiDivContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.UnaryMinusContext;
import lambda.rodeo.lang.types.IntType;
import lambda.rodeo.lang.values.Constant;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

@Slf4j
public class ExpressionAstFactory extends LambdaRodeoBaseListener {

  private ExpressionAst ast;
  private Deque<ExpressionAst> expressionStack = new LinkedList<>();

  public ExpressionAstFactory(ExprContext ctx) {
    ParseTreeWalker.DEFAULT.walk(this, ctx);
  }

  public ExpressionAst toAst() {
    return expressionStack.getLast();
  }

  @Override
  public void exitAddSub(AddSubContext ctx) {
    ExpressionAst rhs = expressionStack.pollLast();
    ExpressionAst lhs = expressionStack.pollLast();
    String op = ctx.addSubOp().getText();

    if ("+".equals(op)) {
      expressionStack.addLast(new AddAst(lhs, rhs));
    } else if ("-".equals(op)) {
      expressionStack.addLast(new SubtractAst(lhs, rhs));
    } else {
      throw new UnsupportedOperationException("Unrecognized add/sub operation '" + op + "'");
    }
  }

  @Override
  public void exitMultiDiv(MultiDivContext ctx) {
    ExpressionAst rhs = expressionStack.pollLast();
    ExpressionAst lhs = expressionStack.pollLast();
    expressionStack.addLast(new MultiplyAst(lhs, rhs));
  }

  @Override
  public void exitUnaryMinus(UnaryMinusContext ctx) {
    ExpressionAst op = expressionStack.pollLast();
    expressionStack.addLast(new UnaryMinusAst(op));
  }

  @Override
  public void enterIntLiteral(IntLiteralContext ctx) {
    BigInteger value = new BigInteger(ctx.getText());
    ConstantExpr<BigInteger> expr = ConstantExpr.<BigInteger>builder()
        .type(IntType.INSTANCE)
        .computable(Constant.<BigInteger>builder().value(value).build())
        .build();
    expressionStack.addLast(expr);
  }
}
