package lambda.rodeo.lang.s1ast;

import java.util.ArrayList;
import java.util.List;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.LrImportContext;
import lambda.rodeo.lang.s1ast.ModuleAst.ModuleAstBuilder;
import lambda.rodeo.lang.antlr.LambdaRodeoBaseListener;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.FunctionDefContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.ModuleContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.ModuleIdentifierContext;
import lambda.rodeo.lang.compilation.S1CompileContext;
import lambda.rodeo.lang.s1ast.functions.FunctionAst;
import lambda.rodeo.lang.s1ast.functions.FunctionAstFactory;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class ModuleAstFactory extends LambdaRodeoBaseListener {

  private ModuleAstBuilder builder = ModuleAst.builder();
  private final S1CompileContext compileContext;
  private final List<FunctionAst> functions = new ArrayList<>();
  private final List<ModuleImportAst> imports = new ArrayList<>();

  public ModuleAstFactory(ModuleContext module, S1CompileContext compileContext) {
    this.compileContext = compileContext;
    ParseTreeWalker.DEFAULT.walk(this, module);
    builder.functionAsts(functions);
    builder.imports(imports);
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


  @Override
  public void enterLrImport(LrImportContext ctx) {
    imports.add(new ModuleImportAstFactory(ctx).toAst());
  }
}
