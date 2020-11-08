package lambda.rodeo.lang.functions;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import lambda.rodeo.lang.CompileUnit;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.FunctionDefContext;
import lambda.rodeo.lang.compilation.CompileError;
import lambda.rodeo.lang.compilation.CompileErrorCollector;
import lambda.rodeo.lang.compilation.S1CompileContext;
import lambda.rodeo.lang.s1ast.functions.FunctionAst;
import lambda.rodeo.lang.s1ast.functions.FunctionAstFactory;
import lambda.rodeo.lang.types.IntType;
import lambda.rodeo.lang.utils.CompileContextUtils;
import lambda.rodeo.lang.utils.CompileUtils;
import lambda.rodeo.lang.utils.TestUtils;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

public class FunctionSignatureTest {

  @Test
  @SneakyThrows
  public void testSignatureDeclaresSameNameTwice() {
    String resource = "/test_cases/functions/ArgNamedTwice.rdo";
    FunctionDefContext functionDef = TestUtils.parseFunctionDef(resource);
    S1CompileContext compileContext = CompileContextUtils.testS1CompileContext();
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

  @Test
  @SneakyThrows
  public void testCallsRightArityWrongType() {
    String resource = "/test_cases/functions/CallRightArityWrongSignature.rdo";
    Supplier<InputStream> interfaceSource = TestUtils.supplyResource(resource);

    CompileUnit interfaceUnit = CompileUnit.builder()
        .contents(interfaceSource)
        .sourcePath("lambda.rodeo.test.InterfaceReturn")
        .build();

    List<CompileUnit> toCompile = new ArrayList<>();
    toCompile.add(interfaceUnit);
    CompileErrorCollector compileErrors = CompileUtils.expectCompileErrors(toCompile);

    assertThat("There were compile errors: \n" + compileErrors,
        compileErrors.getCompileErrors().size(), equalTo(1));
    assertThat(compileErrors.getCompileErrors().get(0).getErrorType(),
        equalTo(CompileError.CALLED_WITH_WRONG_ARGS));
    System.out.println(compileErrors);
  }

  @Test
  @SneakyThrows
  public void testCallsRightArityWrongType2() {
    String resource = "/test_cases/functions/CallRightArityWrongSignature2.rdo";
    Supplier<InputStream> interfaceSource = TestUtils.supplyResource(resource);

    CompileUnit interfaceUnit = CompileUnit.builder()
        .contents(interfaceSource)
        .sourcePath("lambda.rodeo.test.InterfaceReturn")
        .build();

    List<CompileUnit> toCompile = new ArrayList<>();
    toCompile.add(interfaceUnit);
    CompileErrorCollector compileErrors = CompileUtils.expectCompileErrors(toCompile);

    assertThat("There were compile errors: \n" + compileErrors,
        compileErrors.getCompileErrors().size(), equalTo(1));
    assertThat(compileErrors.getCompileErrors().get(0).getErrorType(),
        equalTo(CompileError.CALLED_WITH_WRONG_ARGS));
    System.out.println(compileErrors);
  }

  @Test
  @SneakyThrows
  public void testCallsRightArityWrongType3() {
    String resource = "/test_cases/functions/CallRightArityWrongSignature3.rdo";
    Supplier<InputStream> interfaceSource = TestUtils.supplyResource(resource);

    CompileUnit interfaceUnit = CompileUnit.builder()
        .contents(interfaceSource)
        .sourcePath("lambda.rodeo.test.InterfaceReturn")
        .build();

    List<CompileUnit> toCompile = new ArrayList<>();
    toCompile.add(interfaceUnit);
    CompileErrorCollector compileErrors = CompileUtils.expectCompileErrors(toCompile);

    assertThat("There were compile errors: \n" + compileErrors,
        compileErrors.getCompileErrors().size(), equalTo(1));
    assertThat(compileErrors.getCompileErrors().get(0).getErrorType(),
        equalTo(CompileError.CALLED_WITH_WRONG_ARGS));
    System.out.println(compileErrors);
  }
}
