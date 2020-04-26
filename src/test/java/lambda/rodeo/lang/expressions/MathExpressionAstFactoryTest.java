package lambda.rodeo.lang.expressions;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.math.BigInteger;
import lambda.rodeo.lang.utils.TestUtils;
import lambda.rodeo.lang.antlr.LambdaRodeoParser;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.ExprContext;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.statements.TypeScope;
import lambda.rodeo.lang.types.IntType;
import lambda.rodeo.lang.utils.CompileContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Slf4j
class MathExpressionAstFactoryTest {

  private CompileContext compileContext;

  @BeforeEach()
  public void beforeEach() {
    compileContext = CompileContextUtils.testCompileContext();
  }

  @Test
  public void testAdditionExpr() {
    String expr = "3 + 3";
    LambdaRodeoParser lambdaRodeoParser = TestUtils.parseString(expr);

    ExprContext exprContext = lambdaRodeoParser.expr();
    ExpressionAstFactory expressionAstFactory = new ExpressionAstFactory(exprContext,
        TypeScope.EMPTY, compileContext);
    ExpressionAst expressionAst = expressionAstFactory.toAst();

    assertThat(expressionAst.getType(TypeScope.EMPTY), equalTo(IntType.INSTANCE));


    assertThat(ExpressionTestUtils.compileAndExecute(expressionAst),
        equalTo(BigInteger.valueOf(6)));

    CompileContextUtils.assertNoCompileErrors(compileContext);
  }

  @Test
  public void testAdditionExpr2() {
    String expr = "1 + 2 + 3 + 4 + 5";
    LambdaRodeoParser lambdaRodeoParser = TestUtils.parseString(expr);

    ExprContext exprContext = lambdaRodeoParser.expr();
    ExpressionAstFactory expressionAstFactory = new ExpressionAstFactory(
        exprContext, TypeScope.EMPTY, compileContext);
    ExpressionAst expressionAst = expressionAstFactory.toAst();

    assertThat(expressionAst.getType(TypeScope.EMPTY), equalTo(IntType.INSTANCE));
    assertThat(ExpressionTestUtils.compileAndExecute(expressionAst),
        equalTo(BigInteger.valueOf(15)));

    CompileContextUtils.assertNoCompileErrors(compileContext);
  }

  @Test
  public void testSubtractionExpr() {
    String expr = "3 - 3";
    LambdaRodeoParser lambdaRodeoParser = TestUtils.parseString(expr);

    ExprContext exprContext = lambdaRodeoParser.expr();
    ExpressionAstFactory expressionAstFactory = new ExpressionAstFactory(
        exprContext, TypeScope.EMPTY, compileContext);
    ExpressionAst expressionAst = expressionAstFactory.toAst();

    assertThat(expressionAst.getType(TypeScope.EMPTY), equalTo(IntType.INSTANCE));
    assertThat(ExpressionTestUtils.compileAndExecute(expressionAst),
        equalTo(BigInteger.valueOf(0)));

    CompileContextUtils.assertNoCompileErrors(compileContext);
  }

  @Test
  public void testSubtractionExpr2() {
    String expr = "1 - 2 - 3 - 4 - 5";
    LambdaRodeoParser lambdaRodeoParser = TestUtils.parseString(expr);

    ExprContext exprContext = lambdaRodeoParser.expr();
    ExpressionAstFactory expressionAstFactory = new ExpressionAstFactory(
        exprContext, TypeScope.EMPTY, compileContext);
    ExpressionAst expressionAst = expressionAstFactory.toAst();

    assertThat(expressionAst.getType(TypeScope.EMPTY), equalTo(IntType.INSTANCE));
    assertThat(ExpressionTestUtils.compileAndExecute(expressionAst),
        equalTo(BigInteger.valueOf(1 - 2 - 3 - 4 - 5)));

    CompileContextUtils.assertNoCompileErrors(compileContext);
  }

  @Test
  public void testSubtractionExpr3() {
    String expr = "1 - -3";
    LambdaRodeoParser lambdaRodeoParser = TestUtils.parseString(expr);

    ExprContext exprContext = lambdaRodeoParser.expr();
    ExpressionAstFactory expressionAstFactory = new ExpressionAstFactory(
        exprContext, TypeScope.EMPTY, compileContext);
    ExpressionAst expressionAst = expressionAstFactory.toAst();


    assertThat(expressionAst.getType(TypeScope.EMPTY), equalTo(IntType.INSTANCE));
    assertThat(ExpressionTestUtils.compileAndExecute(expressionAst),
        equalTo(BigInteger.valueOf(1 - -3)));

    CompileContextUtils.assertNoCompileErrors(compileContext);
  }

  @Test
  public void testMultiplyExpr() {
    String expr = "3 * 3";
    LambdaRodeoParser lambdaRodeoParser = TestUtils.parseString(expr);

    ExprContext exprContext = lambdaRodeoParser.expr();
    ExpressionAstFactory expressionAstFactory = new ExpressionAstFactory(
        exprContext, TypeScope.EMPTY, compileContext);
    ExpressionAst expressionAst = expressionAstFactory.toAst();

    assertThat(expressionAst.getType(TypeScope.EMPTY), equalTo(IntType.INSTANCE));
    assertThat(ExpressionTestUtils.compileAndExecute(expressionAst),
        equalTo(BigInteger.valueOf(9)));

    CompileContextUtils.assertNoCompileErrors(compileContext);
  }

  @Test
  public void testMultiplyExpr2() {
    String expr = "1 * 2 * 3 * 4 * 5";
    LambdaRodeoParser lambdaRodeoParser = TestUtils.parseString(expr);

    ExprContext exprContext = lambdaRodeoParser.expr();
    ExpressionAstFactory expressionAstFactory = new ExpressionAstFactory(
        exprContext, TypeScope.EMPTY, compileContext);
    ExpressionAst expressionAst = expressionAstFactory.toAst();

    assertThat(expressionAst.getType(TypeScope.EMPTY), equalTo(IntType.INSTANCE));
    assertThat(ExpressionTestUtils.compileAndExecute(expressionAst),
        equalTo(BigInteger.valueOf(2 * 3 * 4 * 5)));

    CompileContextUtils.assertNoCompileErrors(compileContext);
  }

  @Test
  public void testOrderOfOperations() {
    String expr = "1 + 2 * 3";
    LambdaRodeoParser lambdaRodeoParser = TestUtils.parseString(expr);

    ExprContext exprContext = lambdaRodeoParser.expr();
    ExpressionAstFactory expressionAstFactory = new ExpressionAstFactory(
        exprContext, TypeScope.EMPTY, compileContext);
    ExpressionAst expressionAst = expressionAstFactory.toAst();

    assertThat(expressionAst.getType(TypeScope.EMPTY), equalTo(IntType.INSTANCE));
    assertThat(ExpressionTestUtils.compileAndExecute(expressionAst),
        equalTo(BigInteger.valueOf(1 + 2 * 3)));

    CompileContextUtils.assertNoCompileErrors(compileContext);
  }

  @Test
  public void testOrderOfOperations2() {
    String expr = "(1 + 2) * (3 + 4) * 5 + 6";
    LambdaRodeoParser lambdaRodeoParser = TestUtils.parseString(expr);

    ExprContext exprContext = lambdaRodeoParser.expr();
    ExpressionAstFactory expressionAstFactory = new ExpressionAstFactory(
        exprContext, TypeScope.EMPTY, compileContext);
    ExpressionAst expressionAst = expressionAstFactory.toAst();

    assertThat(expressionAst.getType(TypeScope.EMPTY), equalTo(IntType.INSTANCE));
    assertThat(ExpressionTestUtils.compileAndExecute(expressionAst),
        equalTo(BigInteger.valueOf((1 + 2) * (3 + 4) * 5 + 6)));

    CompileContextUtils.assertNoCompileErrors(compileContext);
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

    CompileContextUtils.assertNoCompileErrors(compileContext);
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

    CompileContextUtils.assertNoCompileErrors(compileContext);
  }

  private void testExpression(String expr, long javaVal) {
    LambdaRodeoParser lambdaRodeoParser = TestUtils.parseString(expr);

    ExprContext exprContext = lambdaRodeoParser.expr();
    ExpressionAstFactory expressionAstFactory = new ExpressionAstFactory(
        exprContext, TypeScope.EMPTY, compileContext);
    ExpressionAst expressionAst = expressionAstFactory.toAst();

    assertThat(expressionAst.getType(TypeScope.EMPTY), equalTo(IntType.INSTANCE));
    assertThat(ExpressionTestUtils.compileAndExecute(expressionAst),
        equalTo(BigInteger.valueOf(javaVal)));

    CompileContextUtils.assertNoCompileErrors(compileContext);
  }
}