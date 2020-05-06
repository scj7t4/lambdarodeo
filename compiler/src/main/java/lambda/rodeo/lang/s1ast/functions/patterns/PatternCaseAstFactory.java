package lambda.rodeo.lang.s1ast.functions.patterns;

import java.util.ArrayList;
import java.util.List;
import lambda.rodeo.lang.antlr.LambdaRodeoBaseListener;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.AtomContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.CaseLiteralContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.CaseVarNameContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.CaseWildCardContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.FunctionBodyContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.IntLiteralContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.LiteralContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.PatternCaseContext;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.s1ast.functions.FunctionBodyAst;
import lambda.rodeo.lang.s1ast.functions.FunctionBodyAstFactory;
import lambda.rodeo.lang.s1ast.functions.patterns.PatternCaseAst.PatternCaseAstBuilder;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class PatternCaseAstFactory extends LambdaRodeoBaseListener {

  private final CompileContext compileContext;
  private PatternCaseAstBuilder astBuilder = PatternCaseAst.builder();
  private final List<CaseArgAst> caseArgAsts = new ArrayList<>();
  private FunctionBodyAst functionBodyAst;

  public PatternCaseAstFactory(PatternCaseContext ctx, CompileContext compileContext) {
    this.compileContext = compileContext;
    ParseTreeWalker.DEFAULT.walk(this, ctx);
  }

  public PatternCaseAst toAst() {
    return astBuilder
        .caseArgAsts(caseArgAsts)
        .functionBodyAst(functionBodyAst)
        .build();
  }


  @Override
  public void enterCaseLiteral(CaseLiteralContext ctx) {
    LiteralContext literal = ctx.literal();
    AtomContext atom = literal.atom();
    if (atom != null) {
      caseArgAsts.add(AtomCaseArg.builder()
          .atom(atom.IDENTIFIER().getText())
          .build());
      return;
    }
    IntLiteralContext intLiteral = literal.intLiteral();
    if (intLiteral != null) {
      caseArgAsts.add(IntLiteralCaseArg.builder()
          .value(intLiteral.getText())
          .build());
    }
  }

  @Override
  public void enterCaseVarName(CaseVarNameContext ctx) {
    String varName = ctx.varName().getText();
    caseArgAsts.add(VariableCaseArg.builder()
        .identifier(varName)
        .build());
  }

  @Override
  public void enterCaseWildCard(CaseWildCardContext ctx) {
    caseArgAsts.add(WildcardCaseArg.builder().build());
  }

  @Override
  public void enterFunctionBody(FunctionBodyContext ctx) {
    FunctionBodyAstFactory functionBodyAstFactory = new FunctionBodyAstFactory(ctx, compileContext);
    functionBodyAst = functionBodyAstFactory.toAst();
  }
}
