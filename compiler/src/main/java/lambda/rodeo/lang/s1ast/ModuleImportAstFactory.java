package lambda.rodeo.lang.s1ast;

import lambda.rodeo.lang.antlr.LambdaRodeoBaseVisitor;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.AliasContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.LrImportContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.ModuleImportContext;

public class ModuleImportAstFactory extends LambdaRodeoBaseVisitor<ModuleImportAst> {

  private ModuleImportAst ast;

  public ModuleImportAstFactory(LrImportContext context) {
    ast = visit(context);
  }

  public ModuleImportAst toAst() {
    return ast;
  }

  @Override
  public ModuleImportAst visitModuleImport(ModuleImportContext ctx) {
    AliasContext alias = ctx.alias();
    String importAlias;

    if (alias != null) {
      importAlias = alias.IDENTIFIER().getText();
    } else {
      String fullModule = ctx.SCOPED_IDENTIFIER().getText();
      importAlias = getSimpleNameForModule(fullModule);
    }

    return ModuleImportAst.builder()
        .startLine(ctx.getStart().getLine())
        .endLine(ctx.getStop().getLine())
        .characterStart(ctx.getStart().getCharPositionInLine())
        .source(ctx.SCOPED_IDENTIFIER().getText())
        .alias(importAlias)
        .build();
  }

  public static String getSimpleNameForModule(String fullModule) {
    String simpleName;
    String[] tokens = fullModule.split("\\.");
    simpleName = tokens[tokens.length - 1];
    return simpleName;
  }
}
