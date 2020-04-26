package lambda.rodeo.lang.expressions;

import java.lang.reflect.Method;
import java.util.Collections;
import lambda.rodeo.lang.ModuleAst;
import lambda.rodeo.lang.functions.FunctionAst;
import lambda.rodeo.lang.functions.FunctionBodyAst;
import lambda.rodeo.lang.functions.FunctionSigAst;
import lambda.rodeo.lang.statements.StatementAst;
import lambda.rodeo.lang.statements.TypeScope;
import lambda.rodeo.lang.utils.CompileUtils;
import lombok.SneakyThrows;

public class ExpressionTestUtils {

  public static final String TEST_METHOD = "testMethod";

  public static ModuleAst moduleForExpression(ExpressionAst expr) {
    return ModuleAst.builder()
        .name("lambda.rodeo.TestHarness")
        .startLine(0)
        .endLine(0)
        .characterStart(0)
        .functionAsts(Collections.singletonList(functionForExpression(expr)))
        .build();
  }

  public static FunctionAst functionForExpression(ExpressionAst expr) {
    return FunctionAst.builder()
        .functionSignature(FunctionSigAst.builder()
            .name(TEST_METHOD)
            .arguments(Collections.emptyList())
            .characterStart(0)
            .startLine(0)
            .endLine(0)
            .build())
        .functionBodyAst(FunctionBodyAst.builder()
            .finalTypeScope(TypeScope.EMPTY)
            .statements(Collections.singletonList(StatementAst.builder()
                .assignment(null)
                .expression(expr)
                .build()))
            .build())
        .build();
  }

  @SneakyThrows
  public static Object compileAndExecute(ExpressionAst expr) {
    Class<?> compiledModule = CompileUtils.createClass(moduleForExpression(expr));
    Method noArgs = compiledModule.getMethod(TEST_METHOD);
    return noArgs.invoke(null);
  }
}
