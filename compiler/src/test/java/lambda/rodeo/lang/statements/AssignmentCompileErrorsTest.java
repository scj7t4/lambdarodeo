package lambda.rodeo.lang.statements;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.List;
import lambda.rodeo.lang.ModuleAstFactory;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.ModuleContext;
import lambda.rodeo.lang.compilation.CompileError;
import lambda.rodeo.lang.compilation.CompileErrorCollector;
import lambda.rodeo.lang.s1ast.ModuleAst;
import lambda.rodeo.lang.utils.CompileContextUtils;
import lambda.rodeo.lang.utils.CompileUtils;
import lambda.rodeo.lang.utils.TestUtils;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

public class AssignmentCompileErrorsTest {

  @Test
  @SneakyThrows
  public void testAlreadyDeclared() {
    String resource = "/test_cases/functions/already_in_scope.rdo";
    ModuleContext moduleDef = TestUtils.parseModule(resource);
    ModuleAstFactory factory = new ModuleAstFactory(moduleDef,
        CompileContextUtils.testCompileContext());
    ModuleAst testCase = factory.toAst();

    CompileErrorCollector compileErrors = CompileUtils.expectCompileErrors(testCase);

    assertThat("There were compile errors: \n" + compileErrors,
        compileErrors.getCompileErrors().size(), equalTo(1));
    assertThat(compileErrors.getCompileErrors().get(0).getErrorType(),
        equalTo(CompileError.IDENTIFIER_ALREADY_IN_SCOPE));
  }
}