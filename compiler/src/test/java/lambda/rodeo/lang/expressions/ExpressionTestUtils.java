package lambda.rodeo.lang.expressions;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import lambda.rodeo.lang.s1ast.ModuleAst;
import lambda.rodeo.lang.s1ast.expressions.ExpressionAst;
import lambda.rodeo.lang.s1ast.functions.FunctionAst;
import lambda.rodeo.lang.s1ast.functions.FunctionBodyAst;
import lambda.rodeo.lang.s1ast.functions.FunctionSigAst;
import lambda.rodeo.lang.s1ast.functions.patterns.PatternCaseAst;
import lambda.rodeo.lang.s1ast.statements.StatementAst;
import lambda.rodeo.lang.utils.CompileUtils;
import lambda.rodeo.runtime.types.Type;
import lombok.SneakyThrows;

public class ExpressionTestUtils {

  public static final String TEST_METHOD = "testMethod";

  public static ModuleAst moduleForExpression(ExpressionAst expr, Type expectedReturnType) {
    List<StatementAst> statements = singleStatementForExpression(expr);
    return moduleForStatements(statements, expectedReturnType);
  }

  public static ModuleAst moduleForStatements(List<StatementAst> statements,
      Type expectedReturnType) {
    return ModuleAst.builder()
        .name("lambda.rodeo.TestHarness")
        .startLine(0)
        .endLine(0)
        .characterStart(0)
        .functionAsts(Collections.singletonList(functionForStatements(statements, expectedReturnType)))
        .build();
  }

  public static FunctionAst functionForStatements(List<StatementAst> statements,
      Type expectedReturnType) {
    return FunctionAst.builder()
        .functionSignature(FunctionSigAst.builder()
            .name(TEST_METHOD)
            .arguments(Collections.emptyList())
            .declaredReturnType(expectedReturnType)
            .characterStart(0)
            .startLine(0)
            .endLine(0)
            .build())
        .functionBodyAst(FunctionBodyAst.builder()
            .patternCases(Collections.singletonList(PatternCaseAst.builder()
                .caseArgs(Collections.emptyList())
                .statements(statements)
                .build()))
            .build())
        .build();
  }

  private static List<StatementAst> singleStatementForExpression(ExpressionAst expr) {
    return Collections.singletonList(StatementAst.builder()
        .assignment(null)
        .expression(expr)
        .build());
  }

  @SneakyThrows
  public static Object compileAndExecute(ExpressionAst expr, Type expectedType) {
    return compileAndExecute(moduleForExpression(expr, expectedType));
  }

  @SneakyThrows
  public static Object compileAndExecute(ModuleAst module) {
    Class<?> compiledModule = CompileUtils.createClass(module);
    Method noArgs = compiledModule.getMethod(TEST_METHOD);
    return noArgs.invoke(null);
  }
}
