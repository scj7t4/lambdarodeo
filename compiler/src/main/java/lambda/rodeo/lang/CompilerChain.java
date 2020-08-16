package lambda.rodeo.lang;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import lambda.rodeo.lang.S3Compiler.CompiledUnit;
import lambda.rodeo.lang.compilation.CompileErrorCollector;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
public class CompilerChain {

  private final List<CompileUnit> compileUnits;
  private final CompileErrorCollector errorCollector = new CompileErrorCollector();

  public CompileResult compile() throws IOException {
    S1Compiler.FinalResult s1Output = S1Compiler.builder()
        .sources(compileUnits)
        .errorCollector(errorCollector)
        .build()
        .compile();

    S2Compiler.TypedFinalResult s2Output = S2Compiler.builder()
        .s1CompileResult(s1Output)
        .errorCollector(errorCollector)
        .build()
        .compile();

    S3Compiler.FinalResult s3Output = S3Compiler.builder()
        .s2CompilerResult(s2Output)
        .errorCollector(errorCollector)
        .build()
        .compile();
    return CompileResult.builder()
        .success(s1Output.isSuccess() && s2Output.isSuccess() && s3Output.isSuccess())
        .compiledUnits(s3Output.getCompiledUnits())
        .errorCollector(errorCollector)
        .build();
  }

  @Builder
  @Getter
  public static class CompileResult {

    private final List<CompiledUnit> compiledUnits;
    @NonNull
    private final CompileErrorCollector errorCollector;
    private final boolean success;

    public Optional<List<CompiledUnit>> getCompiledUnits() {
      return Optional.ofNullable(compiledUnits);
    }
  }
}
