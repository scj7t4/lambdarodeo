package lambda.rodeo.lang.s1ast.expressions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.CallTargetContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.ExprContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.FunctionCallContext;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.s2typed.expressions.TypedExpression;
import lambda.rodeo.lang.scope.TypeScope;
import lambda.rodeo.lang.scope.TypedModuleScope;
import lombok.Builder;
import lombok.Getter;
import org.antlr.v4.runtime.Token;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ExpressionAstFactoryTest {

  @Test
  public void testFunctionCallArgOrder() {
    ExpressionAstFactory testFactory = new ExpressionAstFactory();
    FunctionCallContext mock = Mockito.mock(FunctionCallContext.class);
    Mockito.when(mock.expr()).thenReturn(List.of(
        Mockito.mock(ExprContext.class),
        Mockito.mock(ExprContext.class),
        Mockito.mock(ExprContext.class)
    ));
    CallTargetContext callTargetMock = Mockito.mock(CallTargetContext.class);
    Mockito.when(mock.callTarget()).thenReturn(callTargetMock);
    Mockito.when(mock.getStart()).thenReturn(Mockito.mock(Token.class));
    Mockito.when(mock.getStop()).thenReturn(Mockito.mock(Token.class));

    testFactory.pushOnToStack(TestExpressionAst.builder().num(1).build());
    testFactory.pushOnToStack(TestExpressionAst.builder().num(2).build());
    testFactory.pushOnToStack(TestExpressionAst.builder().num(3).build());

    testFactory.exitFunctionCall(mock);

    FunctionCallAst ast = (FunctionCallAst) testFactory.toAst();
    assertThat(ast.getArgs()
            .stream()
            .map(x -> (TestExpressionAst) x)
            .map(TestExpressionAst::getNum)
            .collect(Collectors.toList()),
        contains(1, 2, 3)
    );
  }

  @Builder
  @Getter
  public static class TestExpressionAst implements ExpressionAst {

    private final int num;

    @Override
    public TypedExpression toTypedExpression(TypeScope scope, TypedModuleScope typedModuleScope,
        CompileContext compileContext) {
      return null;
    }

    @Override
    public int getStartLine() {
      return 0;
    }

    @Override
    public int getEndLine() {
      return 0;
    }

    @Override
    public int getCharacterStart() {
      return 0;
    }
  }
}