package lambda.rodeo.lang.expressions;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.math.BigInteger;
import lambda.rodeo.lang.TestUtils;
import lambda.rodeo.lang.antlr.LambdaRodeoParser;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.ExprContext;
import lambda.rodeo.lang.statement.Scope;
import lambda.rodeo.lang.types.IntType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class ExpressionAstFactoryTest {

  @Test
  public void testAdditionExpr() {
    String expr = "3 + 3";
    LambdaRodeoParser lambdaRodeoParser = TestUtils.parseString(expr);

    ExprContext exprContext = lambdaRodeoParser.expr();
    ExpressionAstFactory expressionAstFactory = new ExpressionAstFactory(exprContext);
    ExpressionAst expressionAst = expressionAstFactory.toAst();

    assertThat(expressionAst.getType(), equalTo(IntType.INSTANCE));
    assertThat(expressionAst.getComputable().compute(Scope.EMPTY),
        equalTo(BigInteger.valueOf(6)));
  }

  @Test
  public void testAdditionExpr2() {
    String expr = "1 + 2 + 3 + 4 + 5";
    LambdaRodeoParser lambdaRodeoParser = TestUtils.parseString(expr);

    ExprContext exprContext = lambdaRodeoParser.expr();
    ExpressionAstFactory expressionAstFactory = new ExpressionAstFactory(exprContext);
    ExpressionAst expressionAst = expressionAstFactory.toAst();

    assertThat(expressionAst.getType(), equalTo(IntType.INSTANCE));
    assertThat(expressionAst.getComputable().compute(Scope.EMPTY),
        equalTo(BigInteger.valueOf(15)));
  }

  @Test
  public void testSubtractionExpr() {
    String expr = "3 - 3";
    LambdaRodeoParser lambdaRodeoParser = TestUtils.parseString(expr);

    ExprContext exprContext = lambdaRodeoParser.expr();
    ExpressionAstFactory expressionAstFactory = new ExpressionAstFactory(exprContext);
    ExpressionAst expressionAst = expressionAstFactory.toAst();

    assertThat(expressionAst.getType(), equalTo(IntType.INSTANCE));
    assertThat(expressionAst.getComputable().compute(Scope.EMPTY),
        equalTo(BigInteger.valueOf(0)));
  }

  @Test
  public void testSubtractionExpr2() {
    String expr = "1 - 2 - 3 - 4 - 5";
    LambdaRodeoParser lambdaRodeoParser = TestUtils.parseString(expr);

    ExprContext exprContext = lambdaRodeoParser.expr();
    ExpressionAstFactory expressionAstFactory = new ExpressionAstFactory(exprContext);
    ExpressionAst expressionAst = expressionAstFactory.toAst();

    log.info(expressionAst.getComputable().toString());

    assertThat(expressionAst.getType(), equalTo(IntType.INSTANCE));
    assertThat(expressionAst.getComputable().compute(Scope.EMPTY),
        equalTo(BigInteger.valueOf(1 - 2 - 3 - 4 - 5)));
  }

  @Test
  public void testSubtractionExpr3() {
    String expr = "1 - -3";
    LambdaRodeoParser lambdaRodeoParser = TestUtils.parseString(expr);

    ExprContext exprContext = lambdaRodeoParser.expr();
    ExpressionAstFactory expressionAstFactory = new ExpressionAstFactory(exprContext);
    ExpressionAst expressionAst = expressionAstFactory.toAst();

    log.info(expressionAst.getComputable().toString());

    assertThat(expressionAst.getType(), equalTo(IntType.INSTANCE));
    assertThat(expressionAst.getComputable().compute(Scope.EMPTY),
        equalTo(BigInteger.valueOf(1 - -3)));
  }

  @Test
  public void testMultiplyExpr() {
    String expr = "3 * 3";
    LambdaRodeoParser lambdaRodeoParser = TestUtils.parseString(expr);

    ExprContext exprContext = lambdaRodeoParser.expr();
    ExpressionAstFactory expressionAstFactory = new ExpressionAstFactory(exprContext);
    ExpressionAst expressionAst = expressionAstFactory.toAst();

    assertThat(expressionAst.getType(), equalTo(IntType.INSTANCE));
    assertThat(expressionAst.getComputable().compute(Scope.EMPTY),
        equalTo(BigInteger.valueOf(9)));
  }

  @Test
  public void testMultiplyExpr2() {
    String expr = "1 * 2 * 3 * 4 * 5";
    LambdaRodeoParser lambdaRodeoParser = TestUtils.parseString(expr);

    ExprContext exprContext = lambdaRodeoParser.expr();
    ExpressionAstFactory expressionAstFactory = new ExpressionAstFactory(exprContext);
    ExpressionAst expressionAst = expressionAstFactory.toAst();

    assertThat(expressionAst.getType(), equalTo(IntType.INSTANCE));
    assertThat(expressionAst.getComputable().compute(Scope.EMPTY),
        equalTo(BigInteger.valueOf(2 * 3 * 4 * 5)));
  }

  @Test
  public void testOrderOfOperations() {
    String expr = "1 + 2 * 3";
    LambdaRodeoParser lambdaRodeoParser = TestUtils.parseString(expr);

    ExprContext exprContext = lambdaRodeoParser.expr();
    ExpressionAstFactory expressionAstFactory = new ExpressionAstFactory(exprContext);
    ExpressionAst expressionAst = expressionAstFactory.toAst();

    assertThat(expressionAst.getType(), equalTo(IntType.INSTANCE));
    assertThat(expressionAst.getComputable().compute(Scope.EMPTY),
        equalTo(BigInteger.valueOf(1 + 2 * 3)));
  }

  @Test
  public void testOrderOfOperations2() {
    String expr = "(1 + 2) * (3 + 4) * 5 + 6";
    LambdaRodeoParser lambdaRodeoParser = TestUtils.parseString(expr);

    ExprContext exprContext = lambdaRodeoParser.expr();
    ExpressionAstFactory expressionAstFactory = new ExpressionAstFactory(exprContext);
    ExpressionAst expressionAst = expressionAstFactory.toAst();

    assertThat(expressionAst.getType(), equalTo(IntType.INSTANCE));
    assertThat(expressionAst.getComputable().compute(Scope.EMPTY),
        equalTo(BigInteger.valueOf((1 + 2) * (3 + 4) * 5 + 6)));
  }

  @Test
  public void testOrderOfOperations3() {
    {
      String expr = "(1 + 2) - 1";
      long javaVal = (1 + 2) - 1;
      testExpression(expr, javaVal);
    }

    {
      String expr = "(1 + 2) - 1 + 2";
      long javaVal = (1 + 2) - 1 + 2;
      testExpression(expr, javaVal);
    }

    {
      String expr = "(1 + 2) - 1 + 2 * (3 + 4)";
      long javaVal = (1 + 2) - 1 + 2 * (3 + 4);
      testExpression(expr, javaVal);
    }

    {
      String expr = "(1 + 2) - 1 + 2 * (3 + 4) - 2";
      long javaVal = (1 + 2) - 1 + 2 * (3 + 4) - 2;
      testExpression(expr, javaVal);
    }

    {
      String expr = "(1 + 2) - 1 + 2 * (3 + 4) - 2 * 5 + 6";
      long javaVal = (1 + 2) - 1 + 2 * (3 + 4) - 2 * 5 + 6;
      testExpression(expr, javaVal);
    }

    {
      String expr = "(1 + 2) - 1 + 2 * (3 + 4) - 2 * 5 + 6";
      long javaVal = (1 + 2) - 1 + 2 * (3 + 4) - 2 * 5 + 6;
      testExpression(expr, javaVal);
    }
  }

  @Test
  public void testOrderOfOperations4() {
    {
      String expr = "(1 + 2) / 1";
      long javaVal = (1 + 2) / 1;
      testExpression(expr, javaVal);
    }

    {
      String expr = "(1 + 2) - 1 / 2";
      long javaVal = (1 + 2) - 1 / 2;
      testExpression(expr, javaVal);
    }

    {
      String expr = "(1 + 2) - 1 + 2 * (3 / 4)";
      long javaVal = (1 + 2) - 1 + 2 * (3 / 4);
      testExpression(expr, javaVal);
    }

    {
      String expr = "(1 + 2) - 1 + 2 * (3 + 4) / 2";
      long javaVal = (1 + 2) - 1 + 2 * (3 + 4) / 2;
      testExpression(expr, javaVal);
    }

    {
      String expr = "(1 + 2) - 1 + 2 * (3 + 4) - 2 * 5 / 6";
      long javaVal = (1 + 2) - 1 + 2 * (3 + 4) - 2 * 5 / 6;
      testExpression(expr, javaVal);
    }

    {
      String expr = "(1 + 2) - 1 / 2 * (3 + 4) - 2 * 5 + 6";
      long javaVal = (1 + 2) - 1 / 2 * (3 + 4) - 2 * 5 + 6;
      testExpression(expr, javaVal);
    }
  }

  private void testExpression(String expr, long javaVal) {
    LambdaRodeoParser lambdaRodeoParser = TestUtils.parseString(expr);

    ExprContext exprContext = lambdaRodeoParser.expr();
    ExpressionAstFactory expressionAstFactory = new ExpressionAstFactory(exprContext);
    ExpressionAst expressionAst = expressionAstFactory.toAst();

    log.info(expressionAst.getComputable().toString());

    assertThat(expressionAst.getType(), equalTo(IntType.INSTANCE));
    assertThat(expressionAst.getComputable().compute(Scope.EMPTY),
        equalTo(BigInteger.valueOf(javaVal)));
  }
}