package lambda.rodeo.lang.utils;

import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.compilation.CompileError;
import lambda.rodeo.lang.s1ast.functions.ToTypedFunctionContext;
import org.hamcrest.collection.IsEmptyCollection;

public class CompileContextUtils {

  public static CompileContext testCompileContext() {
    return CompileContext.builder()
        .source("test")
        .build();
  }

  public static ToTypedFunctionContext testToTypedFunctionContext() {
    return ToTypedFunctionContext.builder()
        .compileContext(testCompileContext())
        .functionName("testFunction")
        .build();
  }

  public static void assertNoCompileErrors(CompileContext compileContext) {
    List<CompileError> compileErrors = compileContext.getCompileErrorCollector()
        .getCompileErrors();
    assertThat("There were compile errors", compileErrors, IsEmptyCollection.empty());
  }

  public static void assertNoCompileErrors(ToTypedFunctionContext compileContext) {
    assertNoCompileErrors(compileContext.getCompileContext());
  }
}
