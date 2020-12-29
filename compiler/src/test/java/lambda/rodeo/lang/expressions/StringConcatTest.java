package lambda.rodeo.lang.expressions;

import static lambda.rodeo.lang.expressions.ExpressionTestUtils.TEST_METHOD;
import static lambda.rodeo.runtime.execution.Trampoline.trampoline;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

import java.lang.reflect.Method;
import lambda.rodeo.lang.antlr.LambdaRodeoParser;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.ExprContext;
import lambda.rodeo.lang.s1ast.ModuleAst;
import lambda.rodeo.lang.s1ast.expressions.ExpressionAst;
import lambda.rodeo.lang.s1ast.expressions.ExpressionAstFactory;
import lambda.rodeo.lang.s1ast.functions.ToTypedFunctionContext;
import lambda.rodeo.lang.scope.TypeScope;
import lambda.rodeo.lang.scope.TypedModuleScope;
import lambda.rodeo.lang.utils.CompileContextUtils;
import lambda.rodeo.lang.utils.CompileUtils;
import lambda.rodeo.lang.utils.TestUtils;
import lambda.rodeo.lang.types.StringType;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class StringConcatTest {

  private ToTypedFunctionContext compileContext;

  @Mock
  TypedModuleScope typedModuleScope;

  @BeforeEach()
  public void beforeEach() {
    compileContext = CompileContextUtils.testToTypedFunctionContext();
  }

  public Class<?> compileExpr(String expr) {
    LambdaRodeoParser lambdaRodeoParser = TestUtils.parseString(expr);

    ExprContext exprContext = lambdaRodeoParser.expr();
    ExpressionAstFactory expressionAstFactory = new ExpressionAstFactory(exprContext,
        CompileContextUtils.testS1CompileContext());
    ExpressionAst expressionAst = expressionAstFactory.toAst();

    assertThat(expressionAst.toTypedExpression(TypeScope.EMPTY,
        typedModuleScope, CompileContextUtils.testToTypedFunctionContext()).getType(),
        equalTo(StringType.INSTANCE));

    ModuleAst moduleAst = ExpressionTestUtils.moduleForExpression(expressionAst,
        StringType.INSTANCE);

    Class<?> retVal = CompileUtils.createClass(moduleAst);

    CompileContextUtils.assertNoCompileErrors(compileContext);

    return retVal;
  }

  @Test
  @SneakyThrows
  public void testBasicString() {
    Class<?> compiledModule = compileExpr("\"Hello World!\"");

    Method noArgs = compiledModule.getMethod(TEST_METHOD);
    String res = (String) trampoline(noArgs.invoke(null));
    assertThat(res, equalTo("Hello World!"));
  }

  @Test
  @SneakyThrows
  public void testBasicStringConcat() {
    Class<?> compiledModule = compileExpr("\"Hello\" + \"World!\"");

    Method noArgs = compiledModule.getMethod(TEST_METHOD);
    String res = (String) trampoline(noArgs.invoke(null));
    assertThat(res, equalTo("HelloWorld!"));
  }

  @Test
  @SneakyThrows
  public void testConcatWithIntRight() {
    Class<?> compiledModule = compileExpr("\"Nice\" + 33");

    Method noArgs = compiledModule.getMethod(TEST_METHOD);

    String res = (String) trampoline(noArgs.invoke(null));
    assertThat(res, equalTo("Nice33"));
  }


  @Test
  @SneakyThrows
  public void testConcatWithIntLeft() {
    Class<?> compiledModule = compileExpr("33 + \"Nice\"");

    Method noArgs = compiledModule.getMethod(TEST_METHOD);

    String res = (String) trampoline(noArgs.invoke(null));
    assertThat(res, equalTo("33Nice"));
  }

  @Test
  @SneakyThrows
  public void testConcatWithIntLeft2() {
    Class<?> compiledModule = compileExpr("33 + 36 + \"Nice\"");

    Method noArgs = compiledModule.getMethod(TEST_METHOD);

    String res = (String) trampoline(noArgs.invoke(null));
    assertThat(res, equalTo("69Nice"));
  }

  @Test
  @SneakyThrows
  public void testConcatWithIntLeft3() {
    Class<?> compiledModule = compileExpr("33 + (36 + \"Nice\")");

    Method noArgs = compiledModule.getMethod(TEST_METHOD);

    String res = (String) trampoline(noArgs.invoke(null));
    assertThat(res, equalTo("3336Nice"));
  }

  @Test
  @SneakyThrows
  public void testConcatWithIntRight2() {
    Class<?> compiledModule = compileExpr("\"Nice\" + 33 + 36");

    Method noArgs = compiledModule.getMethod(TEST_METHOD);

    String res = (String) trampoline(noArgs.invoke(null));
    assertThat(res, equalTo("Nice3336"));
  }

  @Test
  @SneakyThrows
  public void testConcatWithIntRight3() {
    Class<?> compiledModule = compileExpr("\"Nice\" + (33 + 36)");

    Method noArgs = compiledModule.getMethod(TEST_METHOD);

    String res = (String) trampoline(noArgs.invoke(null));
    assertThat(res, equalTo("Nice69"));
  }
}
