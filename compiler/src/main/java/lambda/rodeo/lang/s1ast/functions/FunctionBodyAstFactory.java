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

//TODO: TEST
public class FunctionBodyAstFactory extends LambdaRodeoBaseListener {

  private final List<StatementAst> statements = new ArrayList<>();
  private final List<PatternCaseAst> patternCases = new ArrayList<>();
  private final CompileContext compileContext;

  public FunctionBodyAstFactory(
      FunctionBodyContext ctx,
      CompileContext compileContext) {
    this.compileContext = compileContext;
    ParseTreeWalker.DEFAULT.walk(this, ctx);
  }

  public FunctionBodyAst toAst() {
    if (!statements.isEmpty()) {
      return FunctionBodyAst.builder()
          .statements(statements)
          .patternCases(Collections.emptyList())
          .build();
    } else {
      return FunctionBodyAst.builder()
          .patternCases(patternCases)
          .statements(Collections.emptyList())
          .build();
    }
  }

  @Override
  public void enterStatement(StatementContext ctx) {
    StatementAstFactory statementAstFactory = new StatementAstFactory(ctx);
    StatementAst statementAst = statementAstFactory.toAst();
    statements.add(statementAst);
  }

  @Override
  public void enterPatternCase(PatternCaseContext ctx) {
    PatternCaseAstFactory patternCaseAstFactory = new PatternCaseAstFactory(ctx, compileContext);
    PatternCaseAst patternCaseAst = patternCaseAstFactory.toAst();
    patternCases.add(patternCaseAst);
  }
}
