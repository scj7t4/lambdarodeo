package lambda.rodeo.lang.functions;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;

import java.io.IOException;
import lambda.rodeo.lang.TestUtils;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.FunctionDefContext;
import lambda.rodeo.lang.types.Atom;
import lambda.rodeo.lang.types.IntType;
import org.junit.jupiter.api.Test;

public class FunctionAstFactoryTest {

  @Test
  public void testNoArgsFunction() throws IOException {
    String resource = "/test_cases/functions/no_args_function.rdo";
    FunctionDefContext functionDef = TestUtils.parseFunctionDef(resource);
    FunctionAstFactory factory = new FunctionAstFactory(functionDef);
    FunctionAst functionAst = factory.toAst();
    assertThat(functionAst.getName(), equalTo("noArgs"));
    assertThat(functionAst.getArguments(), empty());
  }

  @Test
  public void testOneArgFunction() throws IOException {
    String resource = "/test_cases/functions/one_arg_function.rdo";
    FunctionDefContext functionDef = TestUtils.parseFunctionDef(resource);
    FunctionAstFactory factory = new FunctionAstFactory(functionDef);
    FunctionAst functionAst = factory.toAst();
    assertThat(functionAst.getName(), equalTo("nillify"));
    assertThat(functionAst.getArguments(), hasSize(1));
    assertThat(functionAst.getArguments().get(0).getName(), equalTo("inp"));
    assertThat(functionAst.getArguments().get(0).getType(), equalTo(new Atom("nil")));
  }

  @Test
  public void testTwoArgFunction() throws IOException {
    String resource = "/test_cases/functions/two_arg_function.rdo";
    FunctionDefContext functionDef = TestUtils.parseFunctionDef(resource);
    FunctionAstFactory factory = new FunctionAstFactory(functionDef);
    FunctionAst functionAst = factory.toAst();
    assertThat(functionAst.getName(), equalTo("add"));
    assertThat(functionAst.getArguments(), hasSize(2));
    assertThat(functionAst.getArguments().get(0).getName(), equalTo("v1"));
    assertThat(functionAst.getArguments().get(0).getType(), equalTo(IntType.INSTANCE));
    assertThat(functionAst.getArguments().get(1).getName(), equalTo("v2"));
    assertThat(functionAst.getArguments().get(1).getType(), equalTo(IntType.INSTANCE));
  }
}
