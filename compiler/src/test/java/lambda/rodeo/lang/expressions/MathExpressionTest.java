package lambda.rodeo.lang.expressions;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

import java.math.BigInteger;
import java.util.List;
import lambda.rodeo.lang.antlr.LambdaRodeoParser;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.ExprContext;
import lambda.rodeo.lang.compilation.CompileError;
import lambda.rodeo.lang.compilation.S1CompileContext;
import lambda.rodeo.lang.s1ast.expressions.ExpressionAst;
import lambda.rodeo.lang.s1ast.expressions.ExpressionAstFactory;
import lambda.rodeo.lang.s1ast.functions.ToTypedFunctionContext;
import lambda.rodeo.lang.scope.TypeScope;
import lambda.rodeo.lang.scope.TypedModuleScope;
import lambda.rodeo.lang.types.CompileableAtom;
import lambda.rodeo.lang.types.IntType;
import lambda.rodeo.lang.utils.CompileContextUtils;
import lambda.rodeo.lang.utils.TestUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Slf4j
@ExtendWith(MockitoExtension.class)
class MathExpressionTest {

  private S1CompileContext compileContext;

  @Mock
  TypedModuleScope typedModuleScope;

  @BeforeEach()
  public void beforeEach() {
    compileContext = CompileContextUtils.testS1CompileContext();
  }

  @Test
  public void testAdditionExpr() {
    String expr = "3 + 3";
    LambdaRodeoParser lambdaRodeoParser = TestUtils.parseString(expr);

    ExprContext exprContext = lambdaRodeoParser.expr();
    ExpressionAstFactory expressionAstFactory = new ExpressionAstFactory(exprContext,
        compileContext);
    ExpressionAst expressionAst = expressionAstFactory.toAst();

    assertThat(
        expressionAst.toTypedExpression(TypeScope.EMPTY,
            typedModuleScope, CompileContextUtils.testToTypedFunctionContext()).getType(),
        equalTo(IntType.INSTANCE));


    assertThat(ExpressionTestUtils.compileAndExecute(expressionAst, IntType.INSTANCE),
        equalTo(BigInteger.valueOf(6)));

    CompileContextUtils.assertNoCompileErrors(compileContext);
  }

  @Test
  public void testAdditionExpr2() {
    String expr = "1 + 2 + 3 + 4 + 5";
    LambdaRodeoParser lambdaRodeoParser = TestUtils.parseString(expr);

    ExprContext exprContext = lambdaRodeoParser.expr();
    ExpressionAstFactory expressionAstFactory = new ExpressionAstFactory(
        exprContext, compileContext);
    ExpressionAst expressionAst = expressionAstFactory.toAst();

    assertThat(expressionAst.toTypedExpression(TypeScope.EMPTY,
        typedModuleScope, CompileContextUtils.testToTypedFunctionContext()).getType(), equalTo(IntType.INSTANCE));
    assertThat(ExpressionTestUtils.compileAndExecute(expressionAst, IntType.INSTANCE),
        equalTo(BigInteger.valueOf(15)));

    CompileContextUtils.assertNoCompileErrors(compileContext);
  }

  @Test
  public void testSubtractionExpr() {
    String expr = "3 - 3";
    LambdaRodeoParser lambdaRodeoParser = TestUtils.parseString(expr);

    ExprContext exprContext = lambdaRodeoParser.expr();
    ExpressionAstFactory expressionAstFactory = new ExpressionAstFactory(
        exprContext, compileContext);
    ExpressionAst expressionAst = expressionAstFactory.toAst();

    assertThat(expressionAst.toTypedExpression(TypeScope.EMPTY,
        typedModuleScope, CompileContextUtils.testToTypedFunctionContext()).getType(), equalTo(IntType.INSTANCE));
    assertThat(ExpressionTestUtils.compileAndExecute(expressionAst, IntType.INSTANCE),
        equalTo(BigInteger.valueOf(0)));

    CompileContextUtils.assertNoCompileErrors(compileContext);
  }

  @Test
  public void testSubtractionExpr2() {
    String expr = "1 - 2 - 3 - 4 - 5";
    LambdaRodeoParser lambdaRodeoParser = TestUtils.parseString(expr);

    ExprContext exprContext = lambdaRodeoParser.expr();
    ExpressionAstFactory expressionAstFactory = new ExpressionAstFactory(
        exprContext, compileContext);
    ExpressionAst expressionAst = expressionAstFactory.toAst();

    assertThat(expressionAst.toTypedExpression(TypeScope.EMPTY,
        typedModuleScope, CompileContextUtils.testToTypedFunctionContext()).getType(), equalTo(IntType.INSTANCE));
    assertThat(ExpressionTestUtils.compileAndExecute(expressionAst, IntType.INSTANCE),
        equalTo(BigInteger.valueOf(1 - 2 - 3 - 4 - 5)));

    CompileContextUtils.assertNoCompileErrors(compileContext);
  }

  @Test
  public void testSubtractionExpr3() {
    String expr = "1 - -3";
    LambdaRodeoParser lambdaRodeoParser = TestUtils.parseString(expr);

    ExprContext exprContext = lambdaRodeoParser.expr();
    ExpressionAstFactory expressionAstFactory = new ExpressionAstFactory(
        exprContext, compileContext);
    ExpressionAst expressionAst = expressionAstFactory.toAst();

    assertThat(expressionAst.toTypedExpression(TypeScope.EMPTY,
        typedModuleScope, CompileContextUtils.testToTypedFunctionContext()).getType(), equalTo(IntType.INSTANCE));
    assertThat(ExpressionTestUtils.compileAndExecute(expressionAst, IntType.INSTANCE),
        equalTo(BigInteger.valueOf(1 - -3)));

    CompileContextUtils.assertNoCompileErrors(compileContext);
  }

  @Test
  public void testMultiplyExpr() {
    String expr = "3 * 3";
    LambdaRodeoParser lambdaRodeoParser = TestUtils.parseString(expr);

    ExprContext exprContext = lambdaRodeoParser.expr();
    ExpressionAstFactory expressionAstFactory = new ExpressionAstFactory(
        exprContext, compileContext);
    ExpressionAst expressionAst = expressionAstFactory.toAst();

    assertThat(expressionAst.toTypedExpression(TypeScope.EMPTY,
        typedModuleScope, CompileContextUtils.testToTypedFunctionContext()).getType(), equalTo(IntType.INSTANCE));
    assertThat(ExpressionTestUtils.compileAndExecute(expressionAst, IntType.INSTANCE),
        equalTo(BigInteger.valueOf(9)));

    CompileContextUtils.assertNoCompileErrors(compileContext);
  }

  @Test
  public void testMultiplyExpr2() {
    String expr = "1 * 2 * 3 * 4 * 5";
    LambdaRodeoParser lambdaRodeoParser = TestUtils.parseString(expr);

    ExprContext exprContext = lambdaRodeoParser.expr();
    ExpressionAstFactory expressionAstFactory = new ExpressionAstFactory(
        exprContext, compileContext);
    ExpressionAst expressionAst = expressionAstFactory.toAst();

    assertThat(expressionAst.toTypedExpression(TypeScope.EMPTY,
        typedModuleScope, CompileContextUtils.testToTypedFunctionContext()).getType(), equalTo(IntType.INSTANCE));
    assertThat(ExpressionTestUtils.compileAndExecute(expressionAst, IntType.INSTANCE),
        equalTo(BigInteger.valueOf(2 * 3 * 4 * 5)));

    CompileContextUtils.assertNoCompileErrors(compileContext);
  }

  @Test
  public void testOrderOfOperations() {
    String expr = "1 + 2 * 3";
    LambdaRodeoParser lambdaRodeoParser = TestUtils.parseString(expr);

    ExprContext exprContext = lambdaRodeoParser.expr();
    ExpressionAstFactory expressionAstFactory = new ExpressionAstFactory(
        exprContext, compileContext);
    ExpressionAst expressionAst = expressionAstFactory.toAst();

    assertThat(expressionAst.toTypedExpression(TypeScope.EMPTY,
        typedModuleScope, CompileContextUtils.testToTypedFunctionContext()).getType(), equalTo(IntType.INSTANCE));
    assertThat(ExpressionTestUtils.compileAndExecute(expressionAst, IntType.INSTANCE),
        equalTo(BigInteger.valueOf(1 + 2 * 3)));

    CompileContextUtils.assertNoCompileErrors(compileContext);
  }

  @Test
  public void testOrderOfOperations2() {
    String expr = "(1 + 2) * (3 + 4) * 5 + 6";
    LambdaRodeoParser lambdaRodeoParser = TestUtils.parseString(expr);

    ExprContext exprContext = lambdaRodeoParser.expr();
    ExpressionAstFactory expressionAstFactory = new ExpressionAstFactory(
        exprContext, compileContext);
    ExpressionAst expressionAst = expressionAstFactory.toAst();

    assertThat(expressionAst.toTypedExpression(TypeScope.EMPTY,
        typedModuleScope, CompileContextUtils.testToTypedFunctionContext()).getType(), equalTo(IntType.INSTANCE));
    assertThat(ExpressionTestUtils.compileAndExecute(expressionAst, IntType.INSTANCE),
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
        exprContext, compileContext);
    ExpressionAst expressionAst = expressionAstFactory.toAst();

    assertThat(expressionAst.toTypedExpression(TypeScope.EMPTY,
        typedModuleScope, CompileContextUtils.testToTypedFunctionContext()).getType(), equalTo(IntType.INSTANCE));
    assertThat(ExpressionTestUtils.compileAndExecute(expressionAst, IntType.INSTANCE),
        equalTo(BigInteger.valueOf(javaVal)));

    CompileContextUtils.assertNoCompileErrors(compileContext);
  }

  @Test
  public void testUndefinedVarAdd() {
    String expr = "3 + @atom";
    LambdaRodeoParser lambdaRodeoParser = TestUtils.parseString(expr);

    ExprContext exprContext = lambdaRodeoParser.expr();
    ExpressionAstFactory expressionAstFactory = new ExpressionAstFactory(exprContext,
        compileContext);
    ExpressionAst expressionAst = expressionAstFactory.toAst();

    ToTypedFunctionContext fnCompileContext = CompileContextUtils.testToTypedFunctionContext();
    assertThat(expressionAst.toTypedExpression(TypeScope.EMPTY,
        typedModuleScope, fnCompileContext).getType(), equalTo(CompileableAtom.UNDEFINED));
    List<CompileError> compileErrors = fnCompileContext.getCompileErrorCollector().getCompileErrors();
    assertThat(compileErrors, hasSize(1));

    CompileError compileError = compileErrors.get(0);
    assertThat(compileError.getStartLine(), equalTo(1));
    assertThat(compileError.getCharacterStart(), equalTo(0));
    assertThat(compileError.getEndLine(), equalTo(1));
    assertThat(compileError.getErrorType(), equalTo(CompileError.ILLEGAL_MATH_OPERATION));
    assertThat(compileError.getErrorMsg(), containsString("@atom"));
    assertThat(compileError.getErrorMsg(), containsString("Int"));
    assertThat(compileError.getErrorMsg(), containsString("addition"));
  }

  @Test
  public void testUndefinedUnaryMinus() {
    String expr = "-@atom";
    LambdaRodeoParser lambdaRodeoParser = TestUtils.parseString(expr);

    ExprContext exprContext = lambdaRodeoParser.expr();
    ExpressionAstFactory expressionAstFactory = new ExpressionAstFactory(exprContext,
        compileContext);
    ExpressionAst expressionAst = expressionAstFactory.toAst();

    ToTypedFunctionContext fnCompileContext = CompileContextUtils.testToTypedFunctionContext();
    assertThat(expressionAst.toTypedExpression(TypeScope.EMPTY,
        typedModuleScope, fnCompileContext).getType(), equalTo(CompileableAtom.UNDEFINED));
    List<CompileError> compileErrors = fnCompileContext.getCompileErrorCollector().getCompileErrors();
    assertThat(compileErrors, hasSize(1));

    CompileError compileError = compileErrors.get(0);
    assertThat(compileError.getStartLine(), equalTo(1));
    assertThat(compileError.getCharacterStart(), equalTo(0));
    assertThat(compileError.getEndLine(), equalTo(1));
    assertThat(compileError.getErrorType(), equalTo(CompileError.ILLEGAL_MATH_OPERATION));
    assertThat(compileError.getErrorMsg(), containsString("@atom"));
    assertThat(compileError.getErrorMsg(), containsString("unary minus"));
  }
}