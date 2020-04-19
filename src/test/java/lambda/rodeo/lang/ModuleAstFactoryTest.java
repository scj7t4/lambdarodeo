package lambda.rodeo.lang;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

import java.io.IOException;
import java.io.InputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.junit.jupiter.api.Test;

class ModuleAstFactoryTest {

  @Test
  public void testEmptyModule() throws IOException {
    InputStream resource = this.getClass().getResourceAsStream("/test_cases/modules/empty.rdo");
    CharStream cs = CharStreams.fromStream(resource);
    ModuleAstFactory factory = new ModuleAstFactory(cs);

    assertThat(factory.toAst().getName(), equalTo("example"));
  }
}