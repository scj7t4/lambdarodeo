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
  public void exitAdd(AddContext ctx) {
    ExpressionAst lhs = expressionStack.pollLast();
    ExpressionAst rhs = expressionStack.pollLast();
    expressionStack.addLast(new AddAst(lhs, rhs));
    log.info("Exit add");
  }

  @Override
  public void exitMultiply(MultiplyContext ctx) {
    log.info("Exit multiply stack, {}", expressionStack);
    ExpressionAst lhs = expressionStack.pollLast();
    ExpressionAst rhs = expressionStack.pollLast();
    expressionStack.addLast(new MultiplyAst(lhs, rhs));
    log.info("Exit multiply {}, {}", lhs, rhs);
  }

  @Override
  public void enterIntLiteral(IntLiteralContext ctx) {
    BigInteger value = new BigInteger(ctx.getText());
    ConstantExpr<BigInteger> expr = ConstantExpr.<BigInteger>builder()
        .type(IntType.INSTANCE)
        .valueHolder(Constant.<BigInteger>builder().value(value).build())
        .build();
    log.info("Encountered intLiteral {}", value);
    expressionStack.addLast(expr);
  }
}
