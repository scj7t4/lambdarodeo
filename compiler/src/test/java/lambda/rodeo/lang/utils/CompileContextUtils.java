package lambda.rodeo.lang.utils;

import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.compilation.CompileError;
import org.hamcrest.collection.IsEmptyCollection;

public class CompileContextUtils {

  public static CompileContext testCompileContext() {
    return CompileContext.builder()
        .source("test")
        .build();
  }

  public static void assertNoCompileErrors(CompileContext compileContext) {
    List<CompileError> compileErrors = compileContext.getCompileErrorCollector()
        .getCompileErrors();
    assertThat("There were compile errors", compileErrors, IsEmptyCollection.empty());
  }
}
