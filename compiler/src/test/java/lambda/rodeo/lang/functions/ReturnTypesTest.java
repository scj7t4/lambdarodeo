package lambda.rodeo.lang.functions;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.math.BigInteger;
import lambda.rodeo.lang.antlr.LambdaRodeoParser;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.ExprContext;
import lambda.rodeo.lang.compilation.CompileError;
import lambda.rodeo.lang.compilation.CompileErrorCollector;
import lambda.rodeo.lang.expressions.ExpressionTestUtils;
import lambda.rodeo.lang.s1ast.ModuleAst;
import lambda.rodeo.lang.s1ast.expressions.ExpressionAst;
import lambda.rodeo.lang.s1ast.expressions.ExpressionAstFactory;
import lambda.rodeo.lang.scope.TypeScope;
import lambda.rodeo.lang.utils.CompileContextUtils;
import lambda.rodeo.lang.utils.CompileUtils;
import lambda.rodeo.lang.utils.TestUtils;
import lambda.rodeo.runtime.types.Atom;
import lambda.rodeo.runtime.types.IntType;
import org.junit.jupiter.api.Test;

public class ReturnTypesTest {

  @Test
  public void testReturnTypeMismatch() {
    String expr = "1 * 2 * 3 * 4 * 5";
    LambdaRodeoParser lambdaRodeoParser = TestUtils.parseString(expr);

    ExprContext exprContext = lambdaRodeoParser.expr();
    ExpressionAstFactory expressionAstFactory = new ExpressionAstFactory(
        exprContext);
    ExpressionAst expressionAst = expressionAstFactory.toAst();

    ModuleAst testCase = ExpressionTestUtils.moduleForExpression(expressionAst, Atom.NULL);
    CompileErrorCollector compileErrors = CompileUtils.expectCompileErrors(testCase);

    assertThat("There were compile errors: \n" + compileErrors,
        compileErrors.getCompileErrors().size(), equalTo(1));
    assertThat(compileErrors.getCompileErrors().get(0).getErrorType(),
        equalTo(CompileError.RETURN_TYPE_MISMATCH));
  }
}
