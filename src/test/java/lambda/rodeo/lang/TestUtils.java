package lambda.rodeo.lang;

import java.io.IOException;
import java.io.InputStream;
import lambda.rodeo.lang.antlr.LambdaRodeoLexer;
import lambda.rodeo.lang.antlr.LambdaRodeoParser;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.FunctionDefContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.ModuleContext;
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

  private static LambdaRodeoParser parseResource(String resource) throws IOException {
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
    InputStream is = ModuleAstFactoryTest.class.getResourceAsStream(resource);
    return CharStreams.fromStream(is);
  }
}
