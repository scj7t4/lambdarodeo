package lambda.rodeo.lang.expressions;

import static lambda.rodeo.lang.expressions.ExpressionTestUtils.TEST_METHOD;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.Collections;
import java.util.function.Supplier;
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
    ExpressionAstFactory expressionAstFactory = new ExpressionAstFactory(exprContext);
    ExpressionAst expressionAst = expressionAstFactory.toAst();

    assertThat(expressionAst.toTypedExpression(TypeScope.EMPTY,
        typedModuleScope, CompileContextUtils.testToTypedFunctionContext()).getType(),
        equalTo(expectedType));

    ModuleAst moduleAst = ExpressionTestUtils.moduleForExpression(expressionAst, expectedType);

    CompileUtils.asmifyModule(moduleAst);

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
}