package lambda.rodeo.lang;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lambda.rodeo.lang.antlr.LambdaRodeoLexer;
import lambda.rodeo.lang.antlr.LambdaRodeoParser;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.ModuleContext;
import lambda.rodeo.lang.compilation.S1CompileContext;
import lambda.rodeo.lang.compilation.S1CompileContextImpl;
import lambda.rodeo.lang.compilation.S2CompileContext;
import lambda.rodeo.lang.compilation.S2CompileContextImpl;
import lambda.rodeo.lang.s1ast.ModuleAst;
import lambda.rodeo.lang.s1ast.ModuleAstFactory;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

@Builder
@Getter
@Slf4j
public class S1Compiler {

  private final List<CompileUnit> sources;

  public S1Compiler.FinalResult compile() throws IOException {
    Map<String, ModuleAst> modules = new HashMap<>();
    boolean noCompileErrors = true;

    for (CompileUnit unit : sources) {
      Result result = compileUnit(unit);
      noCompileErrors &= result.isSuccess();
      modules.put(result.getModuleAst().getName(), result.getModuleAst());
    }

    return FinalResult.builder()
        .success(noCompileErrors)
        .compileContext(S2CompileContextImpl.builder()
            .modules(modules)
            .build())
        .build();
  }

  public Result compileUnit(CompileUnit unit)
      throws IOException {

    try (InputStream is = unit.getContents().get()) {
      CharStream cs = CharStreams.fromStream(is);
      LambdaRodeoLexer moduleLexer = new LambdaRodeoLexer(cs);
      CommonTokenStream cst = new CommonTokenStream(moduleLexer);
      LambdaRodeoParser lambdaRodeoParser = new LambdaRodeoParser(cst);
      ModuleContext module = lambdaRodeoParser.module();
      S1CompileContext compileContext = S1CompileContextImpl.builder()
          .source(unit.getSourcePath())
          .build();
      ModuleAstFactory factory = new ModuleAstFactory(module, compileContext);
      if (!compileContext.getCompileErrorCollector().getCompileErrors().isEmpty()) {
        log.error("Compile Errors For {}:\n{}",
            unit.getSourcePath(),
            compileContext.getCompileErrorCollector());
        return Result.builder()
            .moduleAst(factory.toAst())
            .success(false)
            .build();
      }
      return Result.builder()
          .moduleAst(factory.toAst())
          .success(true)
          .build();
    }
  }

  @Builder
  @Getter
  public static class Result {

    private final boolean success;
    private final ModuleAst moduleAst;
  }

  @Builder
  @Getter
  public static class FinalResult {

    private final boolean success;
    private final S2CompileContext compileContext;
  }
}
