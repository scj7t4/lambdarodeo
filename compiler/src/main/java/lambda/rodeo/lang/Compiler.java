package lambda.rodeo.lang;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import lambda.rodeo.lang.antlr.LambdaRodeoLexer;
import lambda.rodeo.lang.antlr.LambdaRodeoParser;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.ModuleContext;
import lambda.rodeo.lang.compilation.CompileContext;
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
public class Compiler {

  private final List<CompileUnit> sources;

  public boolean compile() throws IOException {
    List<ModuleAst> modules = new ArrayList<>();
    boolean noCompileErrors = true;

    for (CompileUnit unit : sources) {
      noCompileErrors &= compileUnit(modules, unit);
    }



    return noCompileErrors;
  }

  public boolean compileUnit(List<ModuleAst> modules, CompileUnit unit)
      throws IOException {
    try (InputStream is = unit.getContents().get()) {
      CharStream cs = CharStreams.fromStream(is);
      LambdaRodeoLexer moduleLexer = new LambdaRodeoLexer(cs);
      CommonTokenStream cst = new CommonTokenStream(moduleLexer);
      LambdaRodeoParser lambdaRodeoParser = new LambdaRodeoParser(cst);
      ModuleContext module = lambdaRodeoParser.module();
      CompileContext compileContext = CompileContext.builder()
          .source(unit.getSourcePath())
          .build();
      ModuleAstFactory factory = new ModuleAstFactory(module, compileContext);
      modules.add(factory.toAst());

      if (!compileContext.getCompileErrorCollector().getCompileErrors().isEmpty()) {
        log.error("Compile Errors For {}:\n{}",
            unit.getSourcePath(),
            compileContext.getCompileErrorCollector());
        return false;
      }
      return true;
    }
  }
}
