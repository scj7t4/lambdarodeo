package lambda.rodeo.lang;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.IOException;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.ModuleContext;
import org.junit.jupiter.api.Test;

class ModuleAstFactoryTest {

  @Test
  public void testEmptyModule() throws IOException {
    String resource = "/test_cases/modules/empty.rdo";
    ModuleContext module = TestUtils.parseModule(resource);

    ModuleAstFactory factory = new ModuleAstFactory(module);

    assertThat(factory.toAst().getName(), equalTo("EmptyModule"));
  }

  @Test
  public void testEmptyScopedModule() throws IOException {
    String resource = "/test_cases/modules/scopedempty.rdo";
    ModuleContext module = TestUtils.parseModule(resource);
    ModuleAstFactory factory = new ModuleAstFactory(module);

    assertThat(factory.toAst().getName(), equalTo("scope.is.cool.EmptyModule"));
  }

}