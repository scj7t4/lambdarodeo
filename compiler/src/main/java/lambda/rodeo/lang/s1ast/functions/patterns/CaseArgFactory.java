package lambda.rodeo.lang.s1ast.functions.patterns;

import lambda.rodeo.lang.antlr.LambdaRodeoBaseVisitor;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.AtomContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.CaseArgContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.CaseLiteralContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.CaseVarNameContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.CaseWildCardContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.IntLiteralContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.LiteralContext;
import lambda.rodeo.lang.compilation.S1CompileContext;
import lambda.rodeo.lang.types.CompileableAtom;


public class CaseArgFactory extends LambdaRodeoBaseVisitor<CaseArgAst> {

  private final S1CompileContext compileContext;
  private final CaseArgAst ast;

  public CaseArgFactory(S1CompileContext compileContext, CaseArgContext ctx) {
    this.compileContext = compileContext;
    this.ast = visit(ctx);
  }

  public CaseArgAst toAst() {
    return ast;
  }

  @Override
  public CaseArgAst visitCaseLiteral(CaseLiteralContext ctx) {
    LiteralContext literal = ctx.literal();
    AtomContext atom = literal.atom();
    if (atom != null) {
      return AtomCaseArgAst.builder()
          .atom(new CompileableAtom(atom.IDENTIFIER().getText()))
          .startLine(ctx.getStart().getLine())
          .endLine(ctx.getStop().getLine())
          .characterStart(ctx.getStart().getCharPositionInLine())
          .build();
    }
    IntLiteralContext intLiteral = literal.intLiteral();
    if (intLiteral != null) {
      return IntLiteralCaseArgAst.builder()
          .value(intLiteral.getText())
          .startLine(ctx.getStart().getLine())
          .endLine(ctx.getStop().getLine())
          .characterStart(ctx.getStart().getCharPositionInLine())
          .build();
    }
    throw new UnsupportedOperationException("What is this!? Do you need to implement another"
        + " kind of literal?");
  }

  @Override
  public CaseArgAst visitCaseVarName(CaseVarNameContext ctx) {
    String varName = ctx.varName().getText();
    return VariableCaseArgAst.builder()
        .identifier(varName)
        .startLine(ctx.getStart().getLine())
        .endLine(ctx.getStop().getLine())
        .characterStart(ctx.getStart().getCharPositionInLine())
        .build();
  }

  @Override
  public CaseArgAst visitCaseWildCard(CaseWildCardContext ctx) {
    return WildcardCaseArgAst.builder()
        .startLine(ctx.getStart().getLine())
        .endLine(ctx.getStop().getLine())
        .characterStart(ctx.getStart().getCharPositionInLine())
        .build();
  }
}
