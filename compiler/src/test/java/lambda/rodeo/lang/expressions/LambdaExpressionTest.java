package lambda.rodeo.lang.expressions;

import static lambda.rodeo.lang.expressions.ExpressionTestUtils.TEST_METHOD;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.Collections;
import lambda.rodeo.lang.antlr.LambdaRodeoParser;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.ExprContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.FunctionDefContext;
import lambda.rodeo.lang.s1ast.ModuleAst;
import lambda.rodeo.lang.s1ast.expressions.ExpressionAst;
import lambda.rodeo.lang.s1ast.expressions.ExpressionAstFactory;
import lambda.rodeo.lang.s1ast.functions.FunctionAst;
import lambda.rodeo.lang.s1ast.functions.FunctionAstFactory;
import lambda.rodeo.lang.s1ast.functions.ToTypedFunctionContext;
import lambda.rodeo.lang.scope.TypeScope;
import lambda.rodeo.lang.scope.TypedModuleScope;
import lambda.rodeo.lang.utils.CompileContextUtils;
import lambda.rodeo.lang.utils.CompileUtils;
import lambda.rodeo.lang.utils.TestUtils;
import lambda.rodeo.runtime.lambda.Lambda0;
import lambda.rodeo.runtime.lambda.Lambda1;
import lambda.rodeo.runtime.types.Atom;
import lambda.rodeo.runtime.types.IntType;
import lambda.rodeo.runtime.types.Lambda;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class LambdaExpressionTest {

  private ToTypedFunctionContext compileContext;

  @Mock
  TypedModuleScope typedModuleScope;

  @BeforeEach()
  public void beforeEach() {
    compileContext = CompileContextUtils.testToTypedFunctionContext();
  }

  public Class<?> compileLambda(String expr, Lambda expectedType) {
    LambdaRodeoParser lambdaRodeoParser = TestUtils.parseString(expr);

    ExprContext exprContext = lambdaRodeoParser.expr();
    ExpressionAstFactory expressionAstFactory = new ExpressionAstFactory(exprContext,
        compileContext.getCompileContext());
    ExpressionAst expressionAst = expressionAstFactory.toAst();

    assertThat(expressionAst.toTypedExpression(TypeScope.EMPTY,
        typedModuleScope, CompileContextUtils.testToTypedFunctionContext()).getType(),
        equalTo(expectedType));

    ModuleAst moduleAst = ExpressionTestUtils.moduleForExpression(expressionAst, expectedType);

    Class<?> retVal = CompileUtils.createClass(moduleAst);

    CompileContextUtils.assertNoCompileErrors(compileContext);

    return retVal;
  }

  @Test
  @SneakyThrows
  public void testNoArgsLambda() {
    Class<?> compiledModule = compileLambda("() => 1337",
        Lambda.builder()
            .returnType(IntType.INSTANCE)
            .args(Collections.emptyList())
            .build()
    );

    Method noArgs = compiledModule.getMethod(TEST_METHOD);
    @SuppressWarnings("unchecked")
    Lambda0<BigInteger> res = (Lambda0<BigInteger>) noArgs.invoke(null);
    assertThat(res.apply(), equalTo(new BigInteger("1337")));
  }

  @Test
  @SneakyThrows
  public void testOneArgLambda() {
    Class<?> compiledModule = compileLambda("(a::ok) => 1337",
        Lambda.builder()
            .returnType(IntType.INSTANCE)
            .args(Collections.singletonList(new Atom("ok")))
            .build()
    );

    Method noArgs = compiledModule.getMethod(TEST_METHOD);
    @SuppressWarnings("unchecked")
    Lambda1<Atom, BigInteger> res = (Lambda1<Atom, BigInteger>) noArgs.invoke(null);
    assertThat(res.apply(new Atom("ok")), equalTo(new BigInteger("1337")));
  }

  @Test
  @SneakyThrows
  public void testClosure0() {
    String resource = "/test_cases/functions/closure_0.rdo";
    FunctionDefContext functionDef = TestUtils.parseFunctionDef(resource);
    FunctionAstFactory factory = new FunctionAstFactory(functionDef,
        CompileContextUtils.testCompileContext());
    FunctionAst functionAst = factory.toAst();
    assertThat(functionAst.getName(), equalTo("closure0"));
    assertThat(functionAst.getArguments(), hasSize(1));
    assertThat(functionAst.getArguments().get(0).getName(), equalTo("v1"));
    assertThat(functionAst.getArguments().get(0).getType(), equalTo(IntType.INSTANCE));

    Class<?> compiledModule = CompileUtils.createClass(TestUtils.testModule()
        .functionAsts(Collections.singletonList(functionAst))
        .build());

    Method method = compiledModule.getMethod("closure0", BigInteger.class);
    @SuppressWarnings("unchecked")
    Lambda0<BigInteger> invokeResult = (Lambda0<BigInteger>) method.invoke(null, BigInteger.TWO);

    assertThat(invokeResult.apply(), equalTo(BigInteger.TWO));
  }

  @Test
  @SneakyThrows
  public void testClosure1() {
    String resource = "/test_cases/functions/closure_1.rdo";
    FunctionDefContext functionDef = TestUtils.parseFunctionDef(resource);
    FunctionAstFactory factory = new FunctionAstFactory(functionDef,
        CompileContextUtils.testCompileContext());
    FunctionAst functionAst = factory.toAst();
    assertThat(functionAst.getName(), equalTo("closure1"));
    assertThat(functionAst.getArguments(), hasSize(1));
    assertThat(functionAst.getArguments().get(0).getName(), equalTo("v1"));
    assertThat(functionAst.getArguments().get(0).getType(), equalTo(IntType.INSTANCE));

    Class<?> compiledModule = CompileUtils.createClass(TestUtils.testModule()
        .functionAsts(Collections.singletonList(functionAst))
        .build());

    Method method = compiledModule.getMethod("closure1", BigInteger.class);
    @SuppressWarnings("unchecked")
    Lambda1<BigInteger, Lambda0<BigInteger>> invokeResult =
        (Lambda1<BigInteger, Lambda0<BigInteger>>) method.invoke(null, BigInteger.TWO);

    assertThat(invokeResult.apply(BigInteger.TWO).apply(), equalTo(new BigInteger("4")));
  }

  @Test
  @SneakyThrows
  public void testInvoke1() {
    String resource = "/test_cases/functions/invoke_closure_1.rdo";
    FunctionDefContext functionDef = TestUtils.parseFunctionDef(resource);
    FunctionAstFactory factory = new FunctionAstFactory(functionDef,
        CompileContextUtils.testCompileContext());
    FunctionAst functionAst = factory.toAst();
    assertThat(functionAst.getName(), equalTo("closure1"));
    assertThat(functionAst.getArguments(), hasSize(1));
    assertThat(functionAst.getArguments().get(0).getName(), equalTo("v1"));
    assertThat(functionAst.getArguments().get(0).getType(), equalTo(IntType.INSTANCE));

    Class<?> compiledModule = CompileUtils.createClass(TestUtils.testModule()
        .functionAsts(Collections.singletonList(functionAst))
        .build());

    Method method = compiledModule.getMethod("closure1", BigInteger.class);
    BigInteger invokeResult = (BigInteger) method.invoke(null, BigInteger.TWO);

    assertThat(invokeResult, equalTo(new BigInteger("4")));
  }

  @Test
  @SneakyThrows
  public void testInvoke0() {
    String resource = "/test_cases/functions/invoke_closure_0.rdo";
    FunctionDefContext functionDef = TestUtils.parseFunctionDef(resource);
    FunctionAstFactory factory = new FunctionAstFactory(functionDef,
        CompileContextUtils.testCompileContext());
    FunctionAst functionAst = factory.toAst();
    assertThat(functionAst.getName(), equalTo("closure1"));
    assertThat(functionAst.getArguments(), hasSize(1));
    assertThat(functionAst.getArguments().get(0).getName(), equalTo("v1"));
    assertThat(functionAst.getArguments().get(0).getType(), equalTo(IntType.INSTANCE));

    Class<?> compiledModule = CompileUtils.createClass(TestUtils.testModule()
        .functionAsts(Collections.singletonList(functionAst))
        .build());

    Method method = compiledModule.getMethod("closure1", BigInteger.class);
    @SuppressWarnings("unchecked")
    Lambda0<BigInteger> invokeResult = (Lambda0<BigInteger>) method.invoke(null, BigInteger.TWO);

    assertThat(invokeResult.apply(), equalTo(new BigInteger("4")));
  }
}
