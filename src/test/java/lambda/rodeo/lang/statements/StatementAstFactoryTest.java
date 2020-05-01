package lambda.rodeo.lang.statements;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import lambda.rodeo.lang.ModuleAst;
import lambda.rodeo.lang.expressions.ExpressionTestUtils;
import lambda.rodeo.lang.utils.CompileUtils;
import lambda.rodeo.lang.utils.TestUtils;
import lambda.rodeo.lang.antlr.LambdaRodeoParser;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.StatementContext;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.utils.CompileContextUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StatementAstFactoryTest {

  private CompileContext compileContext;

  @BeforeEach
  public void beforeEach() {
    compileContext = CompileContextUtils.testCompileContext();
  }

  @Test
  public void testWithAssignment() {
    String statement1 = "let cheetos=2+3;";
    String statement2 = "cheetos;";


    LambdaRodeoParser lambdaRodeoParser = TestUtils.parseString(statement1);
    StatementContext exprContext = lambdaRodeoParser.statement();
    StatementAstFactory astFactory = new StatementAstFactory(exprContext
    );
    StatementAst ast1 = astFactory.toAst();

    lambdaRodeoParser = TestUtils.parseString(statement2);
    exprContext = lambdaRodeoParser.statement();
    astFactory = new StatementAstFactory(exprContext);
    StatementAst ast2 = astFactory.toAst();

    List<StatementAst> statements = new ArrayList<>();
    statements.add(ast1);
    statements.add(ast2);

    ModuleAst moduleAst = ExpressionTestUtils.moduleForStatements(statements);
    CompileUtils.asmifyModule(moduleAst);

    Object result = ExpressionTestUtils.compileAndExecute(moduleAst);

    assertThat(result, equalTo(BigInteger.valueOf(5)));

    CompileContextUtils.assertNoCompileErrors(compileContext);
  }
}