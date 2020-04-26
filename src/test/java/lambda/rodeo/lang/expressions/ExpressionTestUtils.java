package lambda.rodeo.lang.expressions;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
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
    List<StatementAst> statements = singleStatementForExpression(expr);
    return moduleForStatements(statements);
  }

  public static ModuleAst moduleForStatements(List<StatementAst> statements) {
    return ModuleAst.builder()
        .name("lambda.rodeo.TestHarness")
        .startLine(0)
        .endLine(0)
        .characterStart(0)
        .functionAsts(Collections.singletonList(functionForStatments(statements)))
        .build();
  }

  public static FunctionAst functionForStatments(List<StatementAst> statements) {
    return FunctionAst.builder()
        .functionSignature(FunctionSigAst.builder()
            .name(TEST_METHOD)
            .arguments(Collections.emptyList())
            .characterStart(0)
            .startLine(0)
            .endLine(0)
            .build())
        .functionBodyAst(FunctionBodyAst.of(statements, TypeScope.EMPTY))
        .build();
  }

  private static List<StatementAst> singleStatementForExpression(ExpressionAst expr) {
    return Collections.singletonList(StatementAst.builder()
        .assignment(null)
        .expression(expr)
        .scopeBefore(TypeScope.EMPTY)
        .scopeAfter(TypeScope.EMPTY)
        .build());
  }

  @SneakyThrows
  public static Object compileAndExecute(ExpressionAst expr) {
    return compileAndExecute(moduleForExpression(expr));
  }

  @SneakyThrows
  public static Object compileAndExecute(ModuleAst module) {
    Class<?> compiledModule = CompileUtils.createClass(module);
    Method noArgs = compiledModule.getMethod(TEST_METHOD);
    return noArgs.invoke(null);
  }
}
