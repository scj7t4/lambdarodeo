package lambda.rodeo.lang.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.function.Supplier;
import lambda.rodeo.lang.s1ast.ModuleAst;
import lambda.rodeo.lang.antlr.LambdaRodeoLexer;
import lambda.rodeo.lang.antlr.LambdaRodeoParser;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.FunctionDefContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.ModuleContext;
import lambda.rodeo.lang.util.IoSupplier;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

public class TestUtils {

  private TestUtils() {
    // Util class
  }

  public static ModuleContext parseModule(String resource) throws IOException {
    LambdaRodeoParser moduleParser = parseResource(resource);
    return moduleParser.module();
  }

  public static FunctionDefContext parseFunctionDef(String resource) throws IOException {
    LambdaRodeoParser moduleParser = parseResource(resource);
    return moduleParser.functionDef();
  }

  public static LambdaRodeoParser parseResource(String resource) throws IOException {
    CharStream cs = openSourceFile(resource);

    LambdaRodeoLexer moduleLexer = new LambdaRodeoLexer(cs);
    CommonTokenStream cst = new CommonTokenStream(moduleLexer);
    return new LambdaRodeoParser(cst);
  }

  public static LambdaRodeoParser parseString(String toParse) {
    CharStream stream = CharStreams.fromString(toParse);
    LambdaRodeoLexer moduleLexer = new LambdaRodeoLexer(stream);
    CommonTokenStream cst = new CommonTokenStream(moduleLexer);
    return new LambdaRodeoParser(cst);
  }

  public static CharStream openSourceFile(String resource) throws IOException {
    InputStream is = TestUtils.class.getResourceAsStream(resource);
    return CharStreams.fromStream(is);
  }

  public static IoSupplier<InputStream> supplyResource(String resource) {
    return () -> TestUtils.class.getResourceAsStream(resource);
  }

  public static ModuleAst.ModuleAstBuilder testModule() {
    return ModuleAst.builder()
        .name("lambda.rodeo.TestModule");
  }
}
