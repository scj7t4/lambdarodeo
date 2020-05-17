package lambda.rodeo.lang.s1ast.expressions;

import java.util.ArrayList;
import java.util.List;
import lambda.rodeo.lang.antlr.LambdaRodeoBaseVisitor;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.AddSubContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.AtomContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.ExprContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.FunctionCallContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.IdentifierContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.IntLiteralContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.LambdaContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.MultiDivContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.ParentheticalContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.StringLiteralContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.UnaryMinusContext;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.compilation.CompileError;
import lambda.rodeo.runtime.types.Atom;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringEscapeUtils;

@Slf4j
public class ExpressionAstFactory extends LambdaRodeoBaseVisitor<ExpressionAst> {

  private final ExpressionAst expr;
  private final CompileContext compileContext;

  public ExpressionAstFactory(ExprContext ctx, CompileContext compileContext) {
    this.compileContext = compileContext;
    expr = visit(ctx);
  }

  public ExpressionAst toAst() {
    return expr;
  }

  @Override
  public ExpressionAst visitAddSub(AddSubContext ctx) {
    ExpressionAst lhs = visit(ctx.expr(0));
    ExpressionAst rhs = visit(ctx.expr(1));
    String op = ctx.addSubOp().getText();

    if ("+".equals(op)) {
      return AddAst.builder()
          .lhs(lhs)
          .rhs(rhs)
          .characterStart(ctx.getStart().getCharPositionInLine())
          .endLine(ctx.getStop().getLine())
          .startLine(ctx.getStart().getLine())
          .build();
    } else if ("-".equals(op)) {
      return SubtractAst.builder()
          .lhs(lhs)
          .rhs(rhs)
          .characterStart(ctx.getStart().getCharPositionInLine())
          .endLine(ctx.getStop().getLine())
          .startLine(ctx.getStart().getLine())
          .build();
    } else {
      throw new UnsupportedOperationException("Unrecognized add/sub operation '" + op + "'");
    }
  }

  @Override
  public ExpressionAst visitParenthetical(ParentheticalContext ctx) {
    return visit(ctx.expr());
  }

  public ExpressionAst visitMultiDiv(MultiDivContext ctx) {
    ExpressionAst lhs = visit(ctx.expr(0));
    ExpressionAst rhs = visit(ctx.expr(1));
    String op = ctx.multiDivOp().getText();

    if ("*".equals(op)) {
      return MultiplyAst.builder()
          .lhs(lhs)
          .rhs(rhs)
          .characterStart(ctx.getStart().getCharPositionInLine())
          .endLine(ctx.getStop().getLine())
          .startLine(ctx.getStart().getLine())
          .build();
    } else if ("/".equals(op)) {
      return DivisionAst.builder()
          .lhs(lhs)
          .rhs(rhs)
          .characterStart(ctx.getStart().getCharPositionInLine())
          .endLine(ctx.getStop().getLine())
          .startLine(ctx.getStart().getLine())
          .build();
    } else {
      throw new UnsupportedOperationException(
          "Unrecognized multiply/divide operation '" + op + "'");
    }
  }

  @Override
  public ExpressionAst visitUnaryMinus(UnaryMinusContext ctx) {
    ExpressionAst op = visit(ctx.expr());
    return UnaryMinusAst.builder()
        .operand(op)
        .startLine(ctx.getStart().getLine())
        .endLine(ctx.getStop().getLine())
        .build();
  }

  @Override
  public ExpressionAst visitIntLiteral(IntLiteralContext ctx) {
    return IntConstantAst.builder()
        .literal(ctx.getText())
        .characterStart(ctx.getStart().getCharPositionInLine())
        .endLine(ctx.getStop().getLine())
        .startLine(ctx.getStart().getLine())
        .build();
  }

  @Override
  public ExpressionAst visitAtom(AtomContext ctx) {
    Atom value = new Atom(ctx.IDENTIFIER().getText());
    return AtomAst.builder()
        .atom(value)
        .characterStart(ctx.getStart().getCharPositionInLine())
        .endLine(ctx.getStop().getLine())
        .startLine(ctx.getStart().getLine())
        .build();
  }

  @Override
  public ExpressionAst visitIdentifier(IdentifierContext ctx) {
    String name = ctx.getText();
    return VariableAst.builder()
        .name(name)
        .characterStart(ctx.getStart().getCharPositionInLine())
        .endLine(ctx.getStop().getLine())
        .startLine(ctx.getStart().getLine())
        .build();
  }

  @Override
  public ExpressionAst visitFunctionCall(FunctionCallContext ctx) {
    ExpressionAst callee = visit(ctx.expr(0));
    int size = ctx.expr().size();
    List<ExpressionAst> args = new ArrayList<>();
    for (int i = 1; i < size; i++) {
      ExpressionAst expressionAst = visit(ctx.expr(i));
      args.add(expressionAst);
    }
    return FunctionCallAst.builder()
        .callTarget(callee)
        .args(new ArrayList<>(args))
        .startLine(ctx.getStart().getLine())
        .endLine(ctx.getStop().getLine())
        .characterStart(ctx.getStart().getCharPositionInLine())
        .build();
  }

  @Override
  public ExpressionAst visitStringLiteral(StringLiteralContext ctx) {
    if(ctx.DOUBLEQSTRING() != null) {
      String contents = ctx.DOUBLEQSTRING().getText();
      contents = contents.substring(1, contents.length()-1);
      StringEscapeUtils.unescapeJava(contents);
      return StringLiteralAst.builder()
          .characterStart(ctx.getStart().getCharPositionInLine())
          .startLine(ctx.getStart().getLine())
          .endLine(ctx.getStop().getLine())
          .contents(contents)
          .build();
    } else {
      return AtomAst.builder()
          .atom(Atom.UNDEFINED)
          .build();
    }
  }

  @Override
  public ExpressionAst visitLambda(LambdaContext ctx) {
    LambdaAstFactory lambdaAstFactory = new LambdaAstFactory(ctx, compileContext);
    return lambdaAstFactory.toAst();
  }
}
