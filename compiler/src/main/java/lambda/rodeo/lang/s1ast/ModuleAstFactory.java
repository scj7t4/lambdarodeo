package lambda.rodeo.lang.s1ast;

import java.util.ArrayList;
import java.util.List;
import lambda.rodeo.lang.antlr.LambdaRodeoBaseListener;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.FunctionDefContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.InterfaceDefContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.LrImportContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.ModuleContext;
import lambda.rodeo.lang.compilation.S1CompileContext;
import lambda.rodeo.lang.s1ast.ModuleAst.ModuleAstBuilder;
import lambda.rodeo.lang.s1ast.functions.FunctionAst;
import lambda.rodeo.lang.s1ast.functions.FunctionAstFactory;
import lambda.rodeo.lang.s1ast.type.InterfaceAst;
import lambda.rodeo.lang.s1ast.type.InterfaceAstFactory;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class ModuleAstFactory extends LambdaRodeoBaseListener {

  private ModuleAstBuilder builder = ModuleAst.builder();
  private final S1CompileContext compileContext;
  private final List<FunctionAst> functions = new ArrayList<>();
  private final List<ModuleImportAst> imports = new ArrayList<>();
  private final List<InterfaceAst> interfaces = new ArrayList<>();

  public ModuleAstFactory(ModuleContext module, S1CompileContext compileContext) {
    this.compileContext = compileContext;
    ParseTreeWalker.DEFAULT.walk(this, module);
    builder.functionAsts(functions);
    builder.imports(imports);
    builder.interfaces(interfaces);

    String identifier = compileContext.getSource()
        .replaceAll("\\.rdo$", "")
        .replace("/", ".");

    builder.name(identifier);
  }

  public ModuleAst toAst() {
    return builder.build();
  }

  @Override
  public void enterModule(ModuleContext ctx) {
    builder.startLine(ctx.getStart().getLine());
    // Not sure why this is null for empty files...
    if(ctx.getStop() != null) {
      builder.endLine(ctx.getStop().getLine());
    } else {
      builder.endLine(ctx.getStart().getLine());
    }

    builder.characterStart(ctx.getStart().getCharPositionInLine());
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

  @Override
  public void enterInterfaceDef(InterfaceDefContext ctx) {
    interfaces.add(new InterfaceAstFactory(ctx).getAst());
  }
}
