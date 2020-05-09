package lambda.rodeo.lang.expressions;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

import java.util.List;
import lambda.rodeo.lang.antlr.LambdaRodeoParser;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.ExprContext;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.compilation.CompileError;
import lambda.rodeo.lang.s1ast.expressions.ExpressionAst;
import lambda.rodeo.lang.s1ast.expressions.ExpressionAstFactory;
import lambda.rodeo.lang.scope.TypeScope;
import lambda.rodeo.lang.scope.TypedModuleScope;
import lambda.rodeo.runtime.types.Atom;
import lambda.rodeo.lang.utils.CompileContextUtils;
import lambda.rodeo.lang.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class VariableExpressionTest {

  private CompileContext compileContext;

  @Mock
  TypedModuleScope typedModuleScope;

  @BeforeEach()
  public void beforeEach() {
    compileContext = CompileContextUtils.testCompileContext();
  }

  @Test
  public void testUndefinedVarAdd() {
    String expr = "3 + myVar";
    LambdaRodeoParser lambdaRodeoParser = TestUtils.parseString(expr);

    ExprContext exprContext = lambdaRodeoParser.expr();
    ExpressionAstFactory expressionAstFactory = new ExpressionAstFactory(exprContext
    );
    ExpressionAst expressionAst = expressionAstFactory.toAst();

    assertThat(expressionAst.toTypedExpression(TypeScope.EMPTY,
        typedModuleScope, compileContext).getType(), equalTo(Atom.UNDEFINED));
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
    ExpressionAstFactory expressionAstFactory = new ExpressionAstFactory(exprContext
    );
    ExpressionAst expressionAst = expressionAstFactory.toAst();

    assertThat(expressionAst.toTypedExpression(TypeScope.EMPTY,
        typedModuleScope, compileContext).getType(), equalTo(Atom.UNDEFINED));
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
    ExpressionAstFactory expressionAstFactory = new ExpressionAstFactory(exprContext
    );
    ExpressionAst expressionAst = expressionAstFactory.toAst();

    assertThat(expressionAst.toTypedExpression(TypeScope.EMPTY,
        typedModuleScope, compileContext).getType(), equalTo(Atom.UNDEFINED));
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
    ExpressionAstFactory expressionAstFactory = new ExpressionAstFactory(exprContext
    );
    ExpressionAst expressionAst = expressionAstFactory.toAst();

    assertThat(expressionAst.toTypedExpression(TypeScope.EMPTY,
        typedModuleScope, compileContext).getType(), equalTo(Atom.UNDEFINED));
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
    ExpressionAstFactory expressionAstFactory = new ExpressionAstFactory(exprContext
    );
    ExpressionAst expressionAst = expressionAstFactory.toAst();

    assertThat(expressionAst.toTypedExpression(TypeScope.EMPTY,
        typedModuleScope, compileContext).getType(), equalTo(Atom.UNDEFINED));
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
