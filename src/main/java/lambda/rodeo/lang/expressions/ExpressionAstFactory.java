package lambda.rodeo.lang.expressions;

import java.math.BigInteger;
import java.util.Deque;
import java.util.LinkedList;
import lambda.rodeo.lang.antlr.LambdaRodeoBaseListener;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.AddContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.ExprContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.IntLiteralContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.MultiplyContext;
import lambda.rodeo.lang.types.IntType;
import lambda.rodeo.lang.values.Constant;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

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
  public void exitAdd(AddContext ctx) {
    ExpressionAst rhs = expressionStack.pollLast();
    ExpressionAst lhs = expressionStack.pollLast();
    expressionStack.push(new AddAst(lhs, rhs));
  }

  @Override
  public void exitMultiply(MultiplyContext ctx) {
    ExpressionAst rhs = expressionStack.pollLast();
    ExpressionAst lhs = expressionStack.pollLast();
    expressionStack.push(new MultiplyAst(lhs, rhs));
  }

  @Override
  public void enterIntLiteral(IntLiteralContext ctx) {
    BigInteger value = new BigInteger(ctx.getText());
    ConstantExpr<BigInteger> expr = ConstantExpr.<BigInteger>builder()
        .type(IntType.INSTANCE)
        .valueHolder(Constant.<BigInteger>builder().value(value).build())
        .build();
    expressionStack.push(expr);
  }
}
