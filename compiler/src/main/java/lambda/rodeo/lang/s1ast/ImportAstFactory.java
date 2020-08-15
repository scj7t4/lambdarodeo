package lambda.rodeo.lang.s1ast;

import java.util.List;
import java.util.stream.Collectors;
import lambda.rodeo.lang.antlr.LambdaRodeoBaseVisitor;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.ModuleImportContext;
import org.antlr.v4.runtime.tree.ParseTree;

public class ImportAstFactory extends LambdaRodeoBaseVisitor<ImportAst> {
  private ImportAst ast;

  public ImportAstFactory(ModuleImportContext context) {
    ast = visit(context);
  }

  public ImportAst toAst() {
    return ast;
  }

  @Override
  public ImportAst visitModuleImport(ModuleImportContext ctx) {
    List<String> importedNames = ctx.IDENTIFIER().stream()
        .map(ParseTree::getText)
        .collect(Collectors.toList());
    String source = ctx.SCOPED_IDENTIFIER().getText();

    return ImportAst.builder()
        .startLine(ctx.getStart().getLine())
        .endLine(ctx.getStop().getLine())
        .characterStart(ctx.getStart().getCharPositionInLine())
        .imports(importedNames)
        .source(source)
        .build();
  }
}
