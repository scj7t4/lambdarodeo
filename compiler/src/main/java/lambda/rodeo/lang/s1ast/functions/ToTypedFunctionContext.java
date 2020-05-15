package lambda.rodeo.lang.s1ast.functions;

import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.compilation.CompileContext.CompileContextBuilder;
import lambda.rodeo.lang.compilation.CompileErrorCollector;
import lambda.rodeo.lang.compilation.CompiledModules;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ToTypedFunctionContext {
  private final CompileContext compileContext;
  private final String functionName;

  @Builder.Default
  private int lambdaCounter = -1;

  public String getSource() {
    return compileContext.getSource();
  }

  public CompileErrorCollector getCompileErrorCollector() {
    return compileContext.getCompileErrorCollector();
  }

  public CompiledModules getCompiledModules() {
    return compileContext.getCompiledModules();
  }

  public String allocateLambda() {
    lambdaCounter++;
    return this.functionName + "$" + lambdaCounter;
  }
}
