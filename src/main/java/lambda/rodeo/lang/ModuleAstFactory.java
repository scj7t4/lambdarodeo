package lambda.rodeo.lang;

import java.util.ArrayList;
import java.util.List;
import lambda.rodeo.lang.ast.ModuleAst;
import lambda.rodeo.lang.ast.ModuleAst.ModuleAstBuilder;
import lambda.rodeo.lang.antlr.LambdaRodeoBaseListener;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.FunctionDefContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.ModuleContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.ModuleIdentifierContext;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.ast.functions.FunctionAst;
import lambda.rodeo.lang.ast.functions.FunctionAstFactory;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class ModuleAstFactory extends LambdaRodeoBaseListener {

  private ModuleAstBuilder builder = ModuleAst.builder();
  private final CompileContext compileContext;
  private final List<FunctionAst> functions = new ArrayList<>();

  public ModuleAstFactory(ModuleContext module, CompileContext compileContext) {
    ParseTreeWalker.DEFAULT.walk(this, module);
    this.compileContext = compileContext;
    builder.functionAsts(functions);
  }

  public ModuleAst toAst() {
    return builder.build();
  }

  @Override
  public void enterModule(ModuleContext ctx) {
    builder.startLine(ctx.getStart().getLine());
    builder.endLine(ctx.getStop().getLine());
    builder.characterStart(ctx.getStart().getCharPositionInLine());
  }

  @Override
  public void enterModuleIdentifier(ModuleIdentifierContext ctx) {
    builder = builder.name(ctx.getText());
  }

  @Override
  public void enterFunctionDef(FunctionDefContext ctx) {
    FunctionAstFactory functionAstFactory = new FunctionAstFactory(ctx, compileContext);
    functions.add(functionAstFactory.toAst());
  }
}
