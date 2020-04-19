package lambda.rodeo.lang;

import lambda.rodeo.lang.ModuleAst.ModuleAstBuilder;
import lambda.rodeo.lang.antlr.module.ModuleLexer;
import java.util.List;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;

public class ModuleAstFactory {

  private final ModuleLexer moduleLexer;

  public ModuleAstFactory(CharStream inp) {
    this.moduleLexer = new ModuleLexer(inp);
  }

  public ModuleAst toAst() {
    ModuleAstBuilder builder = ModuleAst.builder();

    Token token = this.moduleLexer.getToken();
    return builder.build();
  }
}
