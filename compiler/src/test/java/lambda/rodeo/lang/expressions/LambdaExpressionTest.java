package lambda.rodeo.lang.expressions;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Collections;
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
import lambda.rodeo.runtime.types.IntType;
import lambda.rodeo.runtime.types.Lambda;
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
  public void testNoArgsLambda() {
    Class<?> aClass = compileLambda("() => 1337",
        Lambda.builder()
            .returnType(IntType.INSTANCE)
            .args(Collections.emptyList())
            .build()
    );

    log.info("here.");
  }
}
