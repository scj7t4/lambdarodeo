package lambda.rodeo.lang.s1ast.functions.patterns;

import java.util.ArrayList;
import java.util.List;
import lambda.rodeo.lang.antlr.LambdaRodeoBaseListener;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.AtomContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.CaseLiteralContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.CaseVarNameContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.CaseWildCardContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.IntLiteralContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.LiteralContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.PatternCaseContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.StatementContext;
import lambda.rodeo.lang.compilation.S1CompileContext;
import lambda.rodeo.lang.s1ast.functions.patterns.PatternCaseAst.PatternCaseAstBuilder;
import lambda.rodeo.lang.s1ast.statements.StatementAst;
import lambda.rodeo.lang.s1ast.statements.StatementAstFactory;
import lambda.rodeo.runtime.types.Atom;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class PatternCaseAstFactory extends LambdaRodeoBaseListener {

  private final S1CompileContext compileContext;
  private final PatternCaseAstBuilder astBuilder;
  private final List<CaseArgAst> caseArg = new ArrayList<>();
  private final List<StatementAst> statements = new ArrayList<>();

  public PatternCaseAstFactory(PatternCaseContext ctx, S1CompileContext compileContext) {
    this.compileContext = compileContext;
    astBuilder = PatternCaseAst.builder()
        .startLine(ctx.getStart().getLine())
        .endLine(ctx.getStop().getLine())
        .characterStart(ctx.getStart().getCharPositionInLine());
    ParseTreeWalker.DEFAULT.walk(this, ctx);
  }

  public PatternCaseAstFactory(S1CompileContext compileContext, Token startToken, Token endToken) {
    this.compileContext = compileContext;
    astBuilder = PatternCaseAst.builder()
        .startLine(startToken.getLine())
        .characterStart(startToken.getCharPositionInLine())
        .endLine(endToken.getCharPositionInLine());
  }

  public PatternCaseAst toAst() {
    return astBuilder
        .caseArgs(caseArg)
        .statements(statements)
        .build();
  }


  @Override
  public void enterCaseLiteral(CaseLiteralContext ctx) {
    LiteralContext literal = ctx.literal();
    AtomContext atom = literal.atom();
    if (atom != null) {
      caseArg.add(AtomCaseArgAst.builder()
          .atom(new Atom(atom.IDENTIFIER().getText()))
          .startLine(ctx.getStart().getLine())
          .endLine(ctx.getStop().getLine())
          .characterStart(ctx.getStart().getCharPositionInLine())
          .build());
      return;
    }
    IntLiteralContext intLiteral = literal.intLiteral();
    if (intLiteral != null) {
      caseArg.add(IntLiteralCaseArgAst.builder()
          .value(intLiteral.getText())
          .startLine(ctx.getStart().getLine())
          .endLine(ctx.getStop().getLine())
          .characterStart(ctx.getStart().getCharPositionInLine())
          .build());
    }
  }

  @Override
  public void enterCaseVarName(CaseVarNameContext ctx) {
    String varName = ctx.varName().getText();
    caseArg.add(VariableCaseArgAst.builder()
        .identifier(varName)
        .startLine(ctx.getStart().getLine())
        .endLine(ctx.getStop().getLine())
        .characterStart(ctx.getStart().getCharPositionInLine())
        .build());
  }

  @Override
  public void enterCaseWildCard(CaseWildCardContext ctx) {
    caseArg.add(WildcardCaseArgAst.builder()
        .startLine(ctx.getStart().getLine())
        .endLine(ctx.getStop().getLine())
        .characterStart(ctx.getStart().getCharPositionInLine())
        .build());
  }

  @Override
  public void enterStatement(StatementContext ctx) {
    StatementAstFactory statementAstFactory = new StatementAstFactory(compileContext, ctx);
    StatementAst statementAst = statementAstFactory.toAst();
    statements.add(statementAst);
  }

}
