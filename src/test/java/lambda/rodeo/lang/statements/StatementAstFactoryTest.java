package lambda.rodeo.lang.statements;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

import java.math.BigInteger;
import lambda.rodeo.lang.TestUtils;
import lambda.rodeo.lang.antlr.LambdaRodeoParser;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.StatementContext;
import lambda.rodeo.lang.utils.OptionalShouldNotBeEmpty;
import org.junit.jupiter.api.Test;

class StatementAstFactoryTest {

  @Test
  public void testWithoutAssignment() {
    String expr = "2+3;";
    LambdaRodeoParser lambdaRodeoParser = TestUtils.parseString(expr);

    StatementContext exprContext = lambdaRodeoParser.statement();
    StatementAstFactory astFactory = new StatementAstFactory(exprContext);

    StatementAst ast = astFactory.toAst();
    assertThat(ast.getAssignment(), nullValue());

    Scope scope = Scope.EMPTY;
    Scope after = ast.compute(scope);
    assertThat(after.get("cheetos").isPresent(), equalTo(false));
    TypedValue $last = after.get("$last").orElseThrow(OptionalShouldNotBeEmpty::new);
    assertThat($last.getValue(), equalTo(BigInteger.valueOf(5)));
    assertThat($last.getValue(), equalTo(BigInteger.valueOf(5)));
  }

  @Test
  public void testWithAssignment() {
    String expr = "let cheetos=2+3;";
    LambdaRodeoParser lambdaRodeoParser = TestUtils.parseString(expr);

    StatementContext exprContext = lambdaRodeoParser.statement();
    StatementAstFactory astFactory = new StatementAstFactory(exprContext);

    StatementAst ast = astFactory.toAst();
    assertThat(ast.getAssignment().getIdentifier(), equalTo("cheetos"));

    Scope scope = Scope.EMPTY;
    Scope after = ast.compute(scope);
    TypedValue $last = after.get("$last").orElseThrow(OptionalShouldNotBeEmpty::new);
    assertThat($last.getValue(), equalTo(BigInteger.valueOf(5)));
    assertThat($last.getValue(), equalTo(BigInteger.valueOf(5)));
    TypedValue cheetos = after.get("cheetos").orElseThrow(OptionalShouldNotBeEmpty::new);
    assertThat(cheetos == $last, equalTo(true));
  }
}