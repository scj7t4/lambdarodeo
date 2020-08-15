package lambda.rodeo.lang.s1ast.functions;

import java.util.Map;
import lambda.rodeo.lang.compilation.CompileErrorCollector;
import lambda.rodeo.lang.compilation.CompiledModules;
import lambda.rodeo.lang.compilation.S2CompileContext;
import lambda.rodeo.lang.s1ast.ModuleAst;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ToTypedFunctionContext {

  private final S2CompileContext compileContext;
  private final String functionName;

  @Builder.Default
  private int lambdaCounter = -1;

  public String getSource() {
    return compileContext.getSource();
  }

  public CompileErrorCollector getCompileErrorCollector() {
    return compileContext.getCompileErrorCollector();
  }

  public Map<String, ModuleAst> getModules() {
    return compileContext.getModules();
  }

  public String allocateLambda() {
    lambdaCounter++;
    return this.functionName + "$" + lambdaCounter;
  }
}
