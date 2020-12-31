package lambda.rodeo.lang.s1ast.functions;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.FunctionBodyContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.PatternCaseContext;
import lambda.rodeo.lang.compilation.S1CompileContext;
import lambda.rodeo.lang.s1ast.functions.patterns.PatternCaseAst;
import lambda.rodeo.lang.s1ast.functions.patterns.PatternCaseAstFactory;
import lambda.rodeo.lang.s1ast.statements.StatementAst;
import lambda.rodeo.lang.s1ast.statements.StatementAstFactory;

public class FunctionBodyAstFactory {

  private final List<PatternCaseAst> patternCases = new ArrayList<>();

  public FunctionBodyAstFactory(
      FunctionBodyContext ctx,
      S1CompileContext compileContext) {

    List<PatternCaseContext> patternCases = ctx.patternCase();
    if (patternCases == null || patternCases.isEmpty()) {
      List<StatementAst> statements = ctx.statement()
          .stream()
          .map(statement -> {
            StatementAstFactory statementAstFactory = new StatementAstFactory(compileContext,
                statement);
            return statementAstFactory.toAst();
          })
          .collect(Collectors.toList());
      PatternCaseAst ast = new PatternCaseAstFactory(compileContext, ctx.getStart(),
          ctx.getStop(), statements).toAst();
      this.patternCases.add(ast);
    } else {
      for (PatternCaseContext patternCase : patternCases) {
        PatternCaseAst patternCaseAst = new PatternCaseAstFactory(patternCase, compileContext)
            .toAst();
        this.patternCases.add(patternCaseAst);
      }
    }
  }

  public FunctionBodyAst toAst() {
    return FunctionBodyAst.builder()
        .patternCases(patternCases)
        .build();
  }
}
