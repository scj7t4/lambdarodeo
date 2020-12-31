package lambda.rodeo.lang.s1ast.functions.patterns;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lambda.rodeo.lang.antlr.LambdaRodeoBaseVisitor;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.PatternCaseContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.StatementContext;
import lambda.rodeo.lang.compilation.S1CompileContext;
import lambda.rodeo.lang.s1ast.functions.patterns.PatternCaseAst.PatternCaseAstBuilder;
import lambda.rodeo.lang.s1ast.statements.StatementAst;
import lambda.rodeo.lang.s1ast.statements.StatementAstFactory;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class PatternCaseAstFactory extends LambdaRodeoBaseVisitor<PatternCaseAst> {

  private final S1CompileContext compileContext;
  private final PatternCaseAst ast;


  public PatternCaseAstFactory(PatternCaseContext ctx, S1CompileContext compileContext) {
    this.compileContext = compileContext;
    ast = visit(ctx);
  }

  public PatternCaseAstFactory(S1CompileContext compileContext,
      Token startToken,
      Token endToken,
      List<StatementAst> statements) {
    this.compileContext = compileContext;
    ast = PatternCaseAst.builder()
        .startLine(startToken.getLine())
        .characterStart(startToken.getCharPositionInLine())
        .endLine(endToken.getLine())
        .statements(statements)
        .caseArgs(Collections.emptyList())
        .build();
  }

  public PatternCaseAst toAst() {
    return ast;
  }

  @Override
  public PatternCaseAst visitPatternCase(PatternCaseContext ctx) {
    List<CaseArgAst> caseArg = ctx.caseArg().stream()
        .map(arg -> {
          CaseArgFactory caseArgFactory = new CaseArgFactory(compileContext, arg);
          return caseArgFactory.toAst();
        })
        .collect(Collectors.toList());
    List<StatementAst> statements = ctx.statement().stream()
        .map(statement -> {
          StatementAstFactory statementAstFactory = new StatementAstFactory(compileContext, statement);
          return statementAstFactory.toAst();
        })
        .collect(Collectors.toList());

    return PatternCaseAst.builder()
        .startLine(ctx.getStart().getLine())
        .endLine(ctx.getStop().getLine())
        .characterStart(ctx.getStart().getCharPositionInLine())
        .caseArgs(caseArg)
        .statements(statements)
        .build();
  }
}
