package lambda.rodeo.lang;

import java.util.ArrayList;
import java.util.List;
import lambda.rodeo.lang.S2Compiler.TypedFinalResult;
import lambda.rodeo.lang.compilation.CompileErrorCollector;
import lambda.rodeo.lang.compilation.S2CompileContext;
import lambda.rodeo.lang.compilation.S2CompileContextImpl;
import lambda.rodeo.lang.s2typed.TypedModule;
import lambda.rodeo.lang.s3compileable.CompileableModule;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
public class S3Compiler {

  @NonNull
  private final TypedFinalResult s2CompilerResult;

  @NonNull
  private final CompileErrorCollector errorCollector;

  public FinalResult compile() {
    List<S2Compiler.TypedModuleResult> typedModules = this.s2CompilerResult.getTypedModules();
    List<CompiledUnit> compiledUnits = new ArrayList<>();


    for (S2Compiler.TypedModuleResult result : typedModules) {
      TypedModule module = result.getModule();
      S2CompileContext context = S2CompileContextImpl.builder()
          .modules(s2CompilerResult.getModules())
          .source(result.getSource())
          .build();
      CompileableModule compileableModule = module.toCompileableModule();
      byte[] compiled = compileableModule.compile(context).get(null);

      compiledUnits.add(CompiledUnit.builder()
          .byteCode(compiled)
          .moduleName(module.getName())
          .source(result.getSource())
          .success(context.getCompileErrorCollector().getCompileErrors().isEmpty())
          .build());
      errorCollector.collectAll(context.getCompileErrorCollector());
    }

    return FinalResult.builder()
        .compiledUnits(compiledUnits)
        .success(errorCollector.getCompileErrors().isEmpty())
        .errorCollector(errorCollector)
        .build();
  }

  @Builder
  @Getter
  public static class FinalResult {

    @NonNull
    private final List<CompiledUnit> compiledUnits;

    private boolean success;

    @NonNull
    private final CompileErrorCollector errorCollector;
  }

  @Builder
  @Getter
  public static class CompiledUnit {

    @NonNull
    private final String moduleName;

    @NonNull
    private final String source;

    @NonNull
    private final byte[] byteCode;

    private final boolean success;
  }
}
