package lambda.rodeo.lang.functions;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

import java.io.IOException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.Collections;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.FunctionDefContext;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.compilation.CompileError;
import lambda.rodeo.lang.compilation.CompileErrorCollector;
import lambda.rodeo.lang.expressions.ExpressionTestUtils;
import lambda.rodeo.lang.s1ast.ModuleAst;
import lambda.rodeo.lang.s1ast.functions.FunctionAst;
import lambda.rodeo.lang.s1ast.functions.FunctionAstFactory;
import lambda.rodeo.lang.utils.CompileContextUtils;
import lambda.rodeo.lang.utils.CompileUtils;
import lambda.rodeo.lang.utils.TestUtils;
import lambda.rodeo.runtime.types.Atom;
import lambda.rodeo.runtime.types.IntType;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

public class FunctionSignatureTest {

  @Test
  @SneakyThrows
  public void testSignatureDeclaresSameNameTwice() {
    String resource = "/test_cases/functions/arg_named_twice.rdo";
    FunctionDefContext functionDef = TestUtils.parseFunctionDef(resource);
    CompileContext compileContext = CompileContextUtils.testCompileContext();
    FunctionAstFactory factory = new FunctionAstFactory(functionDef,
        compileContext);
    FunctionAst functionAst = factory.toAst();
    assertThat(functionAst.getName(), equalTo("add"));
    assertThat(functionAst.getArguments(), hasSize(2));
    assertThat(functionAst.getArguments().get(0).getName(), equalTo("v1"));
    assertThat(functionAst.getArguments().get(0).getType(), equalTo(IntType.INSTANCE));
    assertThat(functionAst.getArguments().get(1).getName(), equalTo("v1"));
    assertThat(functionAst.getArguments().get(1).getType(), equalTo(IntType.INSTANCE));

    CompileErrorCollector compileErrors = compileContext.getCompileErrorCollector();
    assertThat("There were compile errors: \n" + compileErrors,
        compileErrors.getCompileErrors().size(), equalTo(1));
    assertThat(compileErrors.getCompileErrors().get(0).getErrorType(),
        equalTo(CompileError.IDENTIFIER_ALREADY_IN_SCOPE));
  }
}
