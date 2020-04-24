package lambda.rodeo.lang.expressions;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

import java.util.List;
import lambda.rodeo.lang.utils.TestUtils;
import lambda.rodeo.lang.antlr.LambdaRodeoParser;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.ExprContext;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.compilation.CompileError;
import lambda.rodeo.lang.statements.TypeScope;
import lambda.rodeo.lang.types.Atom;
import lambda.rodeo.lang.utils.CompileContextUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class VariableExpressionAstFactoryTest {

  private CompileContext compileContext;

  @BeforeEach()
  public void beforeEach() {
    compileContext = CompileContextUtils.testCompileContext();
  }

  @Test
  public void testUndefinedVarAdd() {
    String expr = "3 + myVar";
    LambdaRodeoParser lambdaRodeoParser = TestUtils.parseString(expr);

    ExprContext exprContext = lambdaRodeoParser.expr();
    ExpressionAstFactory expressionAstFactory = new ExpressionAstFactory(exprContext,
        TypeScope.EMPTY, compileContext);
    ExpressionAst expressionAst = expressionAstFactory.toAst();

    assertThat(expressionAst.getType(TypeScope.EMPTY), equalTo(Atom.UNDEFINED_VAR));
    List<CompileError> compileErrors = compileContext.getCompileErrorCollector().getCompileErrors();
    assertThat(compileErrors, hasSize(1));

    CompileError compileError = compileErrors.get(0);
    assertThat(compileError.getStartLine(), equalTo(1));
    assertThat(compileError.getCharacterStart(), equalTo(4));
    assertThat(compileError.getEndLine(), equalTo(1));
    assertThat(compileError.getErrorType(), equalTo(CompileError.UNDEFINED_VAR));
    assertThat(compileError.getErrorMsg(), containsString("myVar"));
  }

  @Test
  public void testUndefinedVarSub() {
    String expr = "3 - myVar";
    LambdaRodeoParser lambdaRodeoParser = TestUtils.parseString(expr);

    ExprContext exprContext = lambdaRodeoParser.expr();
    ExpressionAstFactory expressionAstFactory = new ExpressionAstFactory(exprContext,
        TypeScope.EMPTY, compileContext);
    ExpressionAst expressionAst = expressionAstFactory.toAst();

    assertThat(expressionAst.getType(TypeScope.EMPTY), equalTo(Atom.UNDEFINED_VAR));
    List<CompileError> compileErrors = compileContext.getCompileErrorCollector().getCompileErrors();
    assertThat(compileErrors, hasSize(1));

    CompileError compileError = compileErrors.get(0);
    assertThat(compileError.getStartLine(), equalTo(1));
    assertThat(compileError.getCharacterStart(), equalTo(4));
    assertThat(compileError.getEndLine(), equalTo(1));
    assertThat(compileError.getErrorType(), equalTo(CompileError.UNDEFINED_VAR));
    assertThat(compileError.getErrorMsg(), containsString("myVar"));
  }

  @Test
  public void testUndefinedVarMul() {
    String expr = "3 * myVar";
    LambdaRodeoParser lambdaRodeoParser = TestUtils.parseString(expr);

    ExprContext exprContext = lambdaRodeoParser.expr();
    ExpressionAstFactory expressionAstFactory = new ExpressionAstFactory(exprContext,
        TypeScope.EMPTY, compileContext);
    ExpressionAst expressionAst = expressionAstFactory.toAst();

    assertThat(expressionAst.getType(TypeScope.EMPTY), equalTo(Atom.UNDEFINED_VAR));
    List<CompileError> compileErrors = compileContext.getCompileErrorCollector().getCompileErrors();
    assertThat(compileErrors, hasSize(1));

    CompileError compileError = compileErrors.get(0);
    assertThat(compileError.getStartLine(), equalTo(1));
    assertThat(compileError.getCharacterStart(), equalTo(4));
    assertThat(compileError.getEndLine(), equalTo(1));
    assertThat(compileError.getErrorType(), equalTo(CompileError.UNDEFINED_VAR));
    assertThat(compileError.getErrorMsg(), containsString("myVar"));
  }

  @Test
  public void testUndefinedVarDiv() {
    String expr = "3 / myVar";
    LambdaRodeoParser lambdaRodeoParser = TestUtils.parseString(expr);

    ExprContext exprContext = lambdaRodeoParser.expr();
    ExpressionAstFactory expressionAstFactory = new ExpressionAstFactory(exprContext,
        TypeScope.EMPTY, compileContext);
    ExpressionAst expressionAst = expressionAstFactory.toAst();

    assertThat(expressionAst.getType(TypeScope.EMPTY), equalTo(Atom.UNDEFINED_VAR));
    List<CompileError> compileErrors = compileContext.getCompileErrorCollector().getCompileErrors();
    assertThat(compileErrors, hasSize(1));

    CompileError compileError = compileErrors.get(0);
    assertThat(compileError.getStartLine(), equalTo(1));
    assertThat(compileError.getCharacterStart(), equalTo(4));
    assertThat(compileError.getEndLine(), equalTo(1));
    assertThat(compileError.getErrorType(), equalTo(CompileError.UNDEFINED_VAR));
    assertThat(compileError.getErrorMsg(), containsString("myVar"));
  }

  @Test
  public void testUndefinedVar() {
    String expr = "myVar";
    LambdaRodeoParser lambdaRodeoParser = TestUtils.parseString(expr);

    ExprContext exprContext = lambdaRodeoParser.expr();
    ExpressionAstFactory expressionAstFactory = new ExpressionAstFactory(exprContext,
        TypeScope.EMPTY, compileContext);
    ExpressionAst expressionAst = expressionAstFactory.toAst();

    assertThat(expressionAst.getType(TypeScope.EMPTY), equalTo(Atom.UNDEFINED_VAR));
    List<CompileError> compileErrors = compileContext.getCompileErrorCollector().getCompileErrors();
    assertThat(compileErrors, hasSize(1));

    CompileError compileError = compileErrors.get(0);
    assertThat(compileError.getStartLine(), equalTo(1));
    assertThat(compileError.getCharacterStart(), equalTo(0));
    assertThat(compileError.getEndLine(), equalTo(1));
    assertThat(compileError.getErrorType(), equalTo(CompileError.UNDEFINED_VAR));
    assertThat(compileError.getErrorMsg(), containsString("myVar"));
  }
}