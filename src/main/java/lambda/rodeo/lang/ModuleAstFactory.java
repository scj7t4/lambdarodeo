package lambda.rodeo.lang;

import lambda.rodeo.lang.ModuleAst.ModuleAstBuilder;
import lambda.rodeo.lang.antlr.LambdaRodeoBaseListener;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.FunctionDefContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.ModuleContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.ModuleIdentifierContext;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class ModuleAstFactory extends LambdaRodeoBaseListener {

  private ModuleAstBuilder builder = ModuleAst.builder();

  public ModuleAstFactory(ModuleContext module) {
    ParseTreeWalker.DEFAULT.walk(this, module);
  }

  public ModuleAst toAst() {
    return builder.build();
  }

  @Override
  public void enterModuleIdentifier(ModuleIdentifierContext ctx) {
    builder = builder.name(ctx.getText());
  }

  @Override
  public void enterFunctionDef(FunctionDefContext ctx) {
    FunctionAstFactory functionAstFactory = new FunctionAstFactory(ctx);
  }
}
