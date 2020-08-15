package lambda.rodeo.lang.s1ast.functions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.FunctionBodyContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.PatternCaseContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.StatementContext;
import lambda.rodeo.lang.compilation.S1CompileContext;
import lambda.rodeo.lang.s1ast.functions.patterns.PatternCaseAst;
import lambda.rodeo.lang.s1ast.functions.patterns.PatternCaseAstFactory;

public class FunctionBodyAstFactory {

  private final List<PatternCaseAst> patternCases = new ArrayList<>();
  private final PatternCaseAstFactory noPatterns;

  public FunctionBodyAstFactory(
      FunctionBodyContext ctx,
      S1CompileContext compileContext) {
    noPatterns = new PatternCaseAstFactory(compileContext, ctx.getStart(), ctx.getStop());

    List<PatternCaseContext> patternCases = ctx.patternCase();
    if(patternCases == null || patternCases.isEmpty()) {
      List<StatementContext> statements = ctx.statement();
      for(StatementContext statement : statements) {
        noPatterns.enterStatement(statement);
      }
    } else {
      for(PatternCaseContext patternCase : patternCases) {
        PatternCaseAst patternCaseAst = new PatternCaseAstFactory(patternCase, compileContext)
            .toAst();
        this.patternCases.add(patternCaseAst);
      }
    }
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
}
