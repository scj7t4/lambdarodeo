package lambda.rodeo.lang.functions;

import static lambda.rodeo.runtime.execution.Trampoline.trampoline;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.Collections;
import lambda.rodeo.lang.s1ast.ModuleAstFactory;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.FunctionDefContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.ModuleContext;
import lambda.rodeo.lang.s1ast.ModuleAst;
import lambda.rodeo.lang.s1ast.functions.FunctionAst;
import lambda.rodeo.lang.s1ast.functions.FunctionAstFactory;
import lambda.rodeo.lang.utils.CompileContextUtils;
import lambda.rodeo.lang.utils.CompileUtils;
import lambda.rodeo.lang.utils.TestUtils;
import lambda.rodeo.runtime.lambda.Lambda0;
import lambda.rodeo.runtime.lambda.Value;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

public class FunctionCasesTest {

  @Test
  @SneakyThrows
  public void testIntLiteralFunctionCase() {
    String resource = "/test_cases/functions/CaseFunctionInt.rdo";
    FunctionDefContext functionDef = TestUtils.parseFunctionDef(resource);
    FunctionAstFactory factory = new FunctionAstFactory(functionDef,
        CompileContextUtils.testS1CompileContext());
    FunctionAst functionAst = factory.toAst();
    ModuleAst testCase = TestUtils.testModule()
        .functionAsts(Collections.singletonList(functionAst))
        .build();

    assertThat(
        testCase.getFunctionAsts().get(0).getFunctionBodyAst().getPatternCases().size(),
        equalTo(2));
    Class<?> compiledModule = CompileUtils.createClass(testCase);

    Method noArgs = compiledModule.getMethod("somefunc", Lambda0.class);
    Object invokeResult = noArgs.invoke(null, Value.of(BigInteger.ONE));
    assertThat(trampoline(invokeResult), equalTo(BigInteger.ONE));

    Object invokeResult2 = noArgs.invoke(null, Value.of(BigInteger.ZERO));
    assertThat(trampoline(invokeResult2), equalTo(BigInteger.ZERO));
  }

  @Test
  @SneakyThrows
  public void testFibonacci() {
    String resource = "/test_cases/functions/CaseFunctionFibo.rdo";
    ModuleContext moduleDef = TestUtils.parseModule(resource);
    ModuleAstFactory factory = new ModuleAstFactory(moduleDef,
        CompileContextUtils.testS1CompileContext());
    ModuleAst testCase = factory.toAst();

    assertThat(
        testCase.getFunctionAsts().get(0).getFunctionBodyAst().getPatternCases().size(),
        equalTo(3));
    Class<?> compiledModule = CompileUtils.createClass(testCase);

    Method func = compiledModule.getMethod("fibonacci", Lambda0.class);
    Object invokeResult = trampoline(func.invoke(null, Value.of(BigInteger.ONE)));
    assertThat(invokeResult, equalTo(BigInteger.ONE));

    invokeResult = trampoline(func.invoke(null, Value.of(BigInteger.ZERO)));
    assertThat(invokeResult, equalTo(BigInteger.ONE));

    invokeResult = trampoline(func.invoke(null, Value.of(BigInteger.valueOf(2))));
    assertThat(invokeResult, equalTo(BigInteger.valueOf(2)));

    invokeResult = trampoline(func.invoke(null, Value.of(BigInteger.valueOf(3))));
    assertThat(invokeResult, equalTo(BigInteger.valueOf(3)));

    invokeResult = trampoline(func.invoke(null, Value.of(BigInteger.valueOf(4))));
    assertThat(invokeResult, equalTo(BigInteger.valueOf(5)));

    Object invokeResult3 = trampoline(func.invoke(null, Value.of(BigInteger.valueOf(8))));
    assertThat(invokeResult3, equalTo(BigInteger.valueOf(34)));

    Object invokeResult4 = trampoline(func.invoke(null, Value.of(BigInteger.valueOf(50))));
    assertThat(invokeResult4, equalTo(BigInteger.valueOf(20365011074L)));
  }
}
