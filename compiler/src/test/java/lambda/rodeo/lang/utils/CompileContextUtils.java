package lambda.rodeo.lang.utils;

import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;
import lambda.rodeo.lang.compilation.CollectsErrors;
import lambda.rodeo.lang.compilation.S1CompileContext;
import lambda.rodeo.lang.compilation.CompileError;
import lambda.rodeo.lang.compilation.S1CompileContextImpl;
import lambda.rodeo.lang.compilation.S2CompileContextImpl;
import lambda.rodeo.lang.s1ast.functions.ToTypedFunctionContext;
import org.hamcrest.collection.IsEmptyCollection;

public class CompileContextUtils {

  public static S1CompileContext testS1CompileContext() {
    return testS1CompileContext("test");
  }

  public static S1CompileContext testS1CompileContext(String source) {
    return S1CompileContextImpl.builder()
        .source(source)
        .build();
  }

  public static S2CompileContextImpl testS2CompileContext() {
    return S2CompileContextImpl.builder()
        .source("test")
        .build();
  }

  public static ToTypedFunctionContext testToTypedFunctionContext() {
    return ToTypedFunctionContext.builder()
        .compileContext(testS2CompileContext())
        .functionName("testFunction")
        .build();
  }

  public static void assertNoCompileErrors(CollectsErrors compileContext) {
    List<CompileError> compileErrors = compileContext.getCompileErrorCollector()
        .getCompileErrors();
    assertThat("There were compile errors", compileErrors, IsEmptyCollection.empty());
  }

  public static void assertNoCompileErrors(ToTypedFunctionContext compileContext) {
    assertNoCompileErrors(compileContext.getCompileContext());
  }
}
