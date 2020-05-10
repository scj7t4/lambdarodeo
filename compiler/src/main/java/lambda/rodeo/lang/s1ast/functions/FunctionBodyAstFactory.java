package lambda.rodeo.lang.s1ast.functions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lambda.rodeo.lang.antlr.LambdaRodeoBaseListener;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.FunctionBodyContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.PatternCaseContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.StatementContext;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.s1ast.functions.patterns.PatternCaseAst;
import lambda.rodeo.lang.s1ast.functions.patterns.PatternCaseAstFactory;
import lambda.rodeo.lang.s1ast.statements.StatementAst;
import lambda.rodeo.lang.s1ast.statements.StatementAstFactory;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class FunctionBodyAstFactory extends LambdaRodeoBaseListener {

  private final List<PatternCaseAst> patternCases = new ArrayList<>();
  private final CompileContext compileContext;
  private final PatternCaseAstFactory noPatterns;

  public FunctionBodyAstFactory(
      FunctionBodyContext ctx,
      CompileContext compileContext) {
    this.compileContext = compileContext;
    noPatterns = new PatternCaseAstFactory(compileContext, ctx.getStart(), ctx.getStop());
    ParseTreeWalker.DEFAULT.walk(this, ctx);
  }

  public FunctionBodyAst toAst() {
    if (!patternCases.isEmpty()) {
      return FunctionBodyAst.builder()
          .patternCases(patternCases)
          .build();
    } else {
      return FunctionBodyAst.builder()
          .patternCases(Collections.singletonList(noPatterns.toAst()))
          .build();
    }
  }

  @Override
  public void enterStatement(StatementContext ctx) {
    noPatterns.enterStatement(ctx);
  }

  @Override
  public void enterPatternCase(PatternCaseContext ctx) {
    PatternCaseAstFactory patternCaseAstFactory = new PatternCaseAstFactory(ctx, compileContext);
    PatternCaseAst patternCaseAst = patternCaseAstFactory.toAst();
    patternCases.add(patternCaseAst);
  }
}
