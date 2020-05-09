package lambda.rodeo.lang.functions;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;

import java.io.IOException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.Collections;
import lambda.rodeo.lang.s1ast.functions.FunctionAst;
import lambda.rodeo.lang.s1ast.functions.FunctionAstFactory;
import lambda.rodeo.lang.utils.CompileContextUtils;
import lambda.rodeo.lang.utils.CompileUtils;
import lambda.rodeo.lang.utils.TestUtils;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.FunctionDefContext;
import lambda.rodeo.runtime.types.Atom;
import lambda.rodeo.runtime.types.IntType;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

public class BasicFunctionTest {

  @SneakyThrows
  @Test
  public void testNoArgsFunction() throws IOException {
    String resource = "/test_cases/functions/no_args_function.rdo";
    FunctionDefContext functionDef = TestUtils.parseFunctionDef(resource);
    FunctionAstFactory factory = new FunctionAstFactory(functionDef,
        CompileContextUtils.testCompileContext());
    FunctionAst functionAst = factory.toAst();
    assertThat(functionAst.getName(), equalTo("noArgs"));
    assertThat(functionAst.getArguments(), empty());

    Class<?> compiledModule = CompileUtils.createClass(TestUtils.testModule()
        .functionAsts(Collections.singletonList(functionAst))
        .build());

    Method noArgs = compiledModule.getMethod("noArgs");
    Object invokeResult = noArgs.invoke(null);

    assertThat(invokeResult, equalTo(Atom.NULL));
  }

  @Test
  @SneakyThrows
  public void testOneArgFunction() throws IOException {
    String resource = "/test_cases/functions/one_arg_function.rdo";
    FunctionDefContext functionDef = TestUtils.parseFunctionDef(resource);
    FunctionAstFactory factory = new FunctionAstFactory(functionDef,
        CompileContextUtils.testCompileContext());
    FunctionAst functionAst = factory.toAst();
    assertThat(functionAst.getName(), equalTo("nillify"));
    assertThat(functionAst.getArguments(), hasSize(1));
    assertThat(functionAst.getArguments().get(0).getName(), equalTo("inp"));
    assertThat(functionAst.getArguments().get(0).getType(), equalTo(new Atom("nil")));

    Class<?> compiledModule = CompileUtils.createClass(TestUtils.testModule()
        .functionAsts(Collections.singletonList(functionAst))
        .build());

    Method noArgs = compiledModule.getMethod("nillify", Atom.class);
    Object invokeResult = noArgs.invoke(null, Atom.NULL);

    assertThat(invokeResult, equalTo(Atom.NULL));
  }

  @Test
  @SneakyThrows
  public void testTwoArgFunction() throws IOException {
    String resource = "/test_cases/functions/two_arg_function.rdo";
    FunctionDefContext functionDef = TestUtils.parseFunctionDef(resource);
    FunctionAstFactory factory = new FunctionAstFactory(functionDef,
        CompileContextUtils.testCompileContext());
    FunctionAst functionAst = factory.toAst();
    assertThat(functionAst.getName(), equalTo("add"));
    assertThat(functionAst.getArguments(), hasSize(2));
    assertThat(functionAst.getArguments().get(0).getName(), equalTo("v1"));
    assertThat(functionAst.getArguments().get(0).getType(), equalTo(IntType.INSTANCE));
    assertThat(functionAst.getArguments().get(1).getName(), equalTo("v2"));
    assertThat(functionAst.getArguments().get(1).getType(), equalTo(IntType.INSTANCE));

    Class<?> compiledModule = CompileUtils.createClass(TestUtils.testModule()
        .functionAsts(Collections.singletonList(functionAst))
        .build());

    Method noArgs = compiledModule.getMethod("add", BigInteger.class, BigInteger.class);
    Object invokeResult = noArgs.invoke(null, BigInteger.valueOf(2), BigInteger.valueOf(2));

    assertThat(invokeResult, instanceOf(BigInteger.class));

    assertThat(invokeResult, equalTo(BigInteger.valueOf(4)));
  }
}