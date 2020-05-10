package lambda.rodeo.lang.functions;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.Collections;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.FunctionDefContext;
import lambda.rodeo.lang.s1ast.ModuleAst;
import lambda.rodeo.lang.s1ast.functions.FunctionAst;
import lambda.rodeo.lang.s1ast.functions.FunctionAstFactory;
import lambda.rodeo.lang.utils.CompileContextUtils;
import lambda.rodeo.lang.utils.CompileUtils;
import lambda.rodeo.lang.utils.TestUtils;
import lambda.rodeo.runtime.types.Atom;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

public class FunctionCasesTest {

  @Test
  @SneakyThrows
  public void testIntLiteralFunctionCase() {
    String resource = "/test_cases/functions/case_function_int.rdo";
    FunctionDefContext functionDef = TestUtils.parseFunctionDef(resource);
    FunctionAstFactory factory = new FunctionAstFactory(functionDef,
        CompileContextUtils.testCompileContext());
    FunctionAst functionAst = factory.toAst();
    ModuleAst testCase = TestUtils.testModule()
        .functionAsts(Collections.singletonList(functionAst))
        .build();

    assertThat(
        testCase.getFunctionAsts().get(0).getFunctionBodyAst().getPatternCases().size(),
        equalTo(2));
    Class<?> compiledModule = CompileUtils.createClass(testCase);

    CompileUtils.asmifyModule(testCase);

    Method noArgs = compiledModule.getMethod("somefunc", BigInteger.class);
    Object invokeResult = noArgs.invoke(null, BigInteger.ONE);
    assertThat(invokeResult, equalTo(BigInteger.ONE));

    Object invokeResult2 = noArgs.invoke(null, BigInteger.ZERO);
    assertThat(invokeResult2, equalTo(BigInteger.ZERO));
  }
}
