package lambda.rodeo.lang.statements;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import lambda.rodeo.lang.s1ast.statements.AssignmentAstFactory;
import lambda.rodeo.lang.s1ast.statements.SimpleAssignmentAst;
import lambda.rodeo.lang.utils.TestUtils;
import lambda.rodeo.lang.antlr.LambdaRodeoParser;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.AssignmentContext;
import org.junit.jupiter.api.Test;

class AssignmentTest {

  @Test
  public void testAssignment() {
    String expr = "let cheetos=";
    LambdaRodeoParser lambdaRodeoParser = TestUtils.parseString(expr);

    AssignmentContext exprContext = lambdaRodeoParser.assignment();
    AssignmentAstFactory assignmentAstFactory = new AssignmentAstFactory(exprContext);

    SimpleAssignmentAst simpleAssignmentAst = assignmentAstFactory.toAst();
    assertThat(simpleAssignmentAst.getIdentifier(), equalTo("cheetos"));
  }
}