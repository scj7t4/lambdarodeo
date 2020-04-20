package lambda.rodeo.lang.expressions;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.math.BigInteger;
import lambda.rodeo.lang.TestUtils;
import lambda.rodeo.lang.antlr.LambdaRodeoParser;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.ExprContext;
import lambda.rodeo.lang.types.IntType;
import org.junit.jupiter.api.Test;

class ExpressionAstFactoryTest {

  @Test
  public void testAdditionExpr() {
    String expr = "3 + 3";
    LambdaRodeoParser lambdaRodeoParser = TestUtils.parseString(expr);

    ExprContext exprContext = lambdaRodeoParser.expr();
    ExpressionAstFactory expressionAstFactory = new ExpressionAstFactory(exprContext);
    ExpressionAst expressionAst = expressionAstFactory.toAst();

    assertThat(expressionAst.getType(), equalTo(IntType.INSTANCE));
    assertThat(expressionAst.getValueHolder().getValue(), equalTo(BigInteger.valueOf(6)));
  }

  @Test
  public void testAdditionExpr2() {
    String expr = "1 + 2 + 3 + 4 + 5";
    LambdaRodeoParser lambdaRodeoParser = TestUtils.parseString(expr);

    ExprContext exprContext = lambdaRodeoParser.expr();
    ExpressionAstFactory expressionAstFactory = new ExpressionAstFactory(exprContext);
    ExpressionAst expressionAst = expressionAstFactory.toAst();

    assertThat(expressionAst.getType(), equalTo(IntType.INSTANCE));
    assertThat(expressionAst.getValueHolder().getValue(), equalTo(BigInteger.valueOf(15)));
  }

  @Test
  public void testMultiplyExpr() {
    String expr = "3 * 3";
    LambdaRodeoParser lambdaRodeoParser = TestUtils.parseString(expr);

    ExprContext exprContext = lambdaRodeoParser.expr();
    ExpressionAstFactory expressionAstFactory = new ExpressionAstFactory(exprContext);
    ExpressionAst expressionAst = expressionAstFactory.toAst();

    assertThat(expressionAst.getType(), equalTo(IntType.INSTANCE));
    assertThat(expressionAst.getValueHolder().getValue(), equalTo(BigInteger.valueOf(9)));
  }

  @Test
  public void testMultiplyExpr2() {
    String expr = "1 * 2 * 3 * 4 * 5";
    LambdaRodeoParser lambdaRodeoParser = TestUtils.parseString(expr);

    ExprContext exprContext = lambdaRodeoParser.expr();
    ExpressionAstFactory expressionAstFactory = new ExpressionAstFactory(exprContext);
    ExpressionAst expressionAst = expressionAstFactory.toAst();

    assertThat(expressionAst.getType(), equalTo(IntType.INSTANCE));
    assertThat(expressionAst.getValueHolder().getValue(),
        equalTo(BigInteger.valueOf(2 * 3 * 4 * 5)));
  }

  @Test
  public void testOrderOfOperations() {
    String expr = "1 + 2 * 3";
    LambdaRodeoParser lambdaRodeoParser = TestUtils.parseString(expr);

    ExprContext exprContext = lambdaRodeoParser.expr();
    ExpressionAstFactory expressionAstFactory = new ExpressionAstFactory(exprContext);
    ExpressionAst expressionAst = expressionAstFactory.toAst();

    System.out.println(expressionAst.getValueHolder().toString());

    assertThat(expressionAst.getType(), equalTo(IntType.INSTANCE));
    assertThat(expressionAst.getValueHolder().getValue(),
        equalTo(BigInteger.valueOf(1 + 2 * 3)));
  }
}