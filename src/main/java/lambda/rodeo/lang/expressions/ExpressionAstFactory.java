package lambda.rodeo.lang.expressions;

import java.util.Deque;
import java.util.LinkedList;
import lambda.rodeo.lang.antlr.LambdaRodeoBaseListener;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.AddSubContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.AtomContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.ExprContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.IdentifierContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.IntLiteralContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.MultiDivContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.UnaryMinusContext;
import lambda.rodeo.lang.types.Atom;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

@Slf4j
public class ExpressionAstFactory extends LambdaRodeoBaseListener {

  private final Deque<ExpressionAst> expressionStack = new LinkedList<>();

  public ExpressionAstFactory(ExprContext ctx) {
    ParseTreeWalker.DEFAULT.walk(this, ctx);
  }

  public ExpressionAst toAst() {
    return expressionStack.getLast();
  }

  @Override
  public void exitAddSub(AddSubContext ctx) {
    ExpressionAst rhs = expressionStack.pollLast();
    ExpressionAst lhs = expressionStack.pollLast();
    String op = ctx.addSubOp().getText();

    if ("+".equals(op)) {
      expressionStack.addLast(AddAst.builder()
          .lhs(lhs)
          .rhs(rhs)
          .characterStart(ctx.getStart().getCharPositionInLine())
          .endLine(ctx.getStop().getLine())
          .startLine(ctx.getStart().getLine())
          .build());
    } else if ("-".equals(op)) {
      expressionStack.addLast(SubtractAst.builder()
          .lhs(lhs)
          .rhs(rhs)
          .characterStart(ctx.getStart().getCharPositionInLine())
          .endLine(ctx.getStop().getLine())
          .startLine(ctx.getStart().getLine())
          .build());
    } else {
      throw new UnsupportedOperationException("Unrecognized add/sub operation '" + op + "'");
    }
  }

  @Override
  public void exitMultiDiv(MultiDivContext ctx) {
    ExpressionAst rhs = expressionStack.pollLast();
    ExpressionAst lhs = expressionStack.pollLast();
    String op = ctx.multiDivOp().getText();

    if ("*".equals(op)) {
      expressionStack.addLast(MultiplyAst.builder()
          .lhs(lhs)
          .rhs(rhs)
          .characterStart(ctx.getStart().getCharPositionInLine())
          .endLine(ctx.getStop().getLine())
          .startLine(ctx.getStart().getLine())
          .build());
    } else if ("/".equals(op)) {
      expressionStack.addLast(DivisionAst.builder().lhs(lhs).rhs(rhs).build());
    } else {
      throw new UnsupportedOperationException(
          "Unrecognized multiply/divide operation '" + op + "'");
    }
  }

  @Override
  public void exitUnaryMinus(UnaryMinusContext ctx) {
    ExpressionAst op = expressionStack.pollLast();
    expressionStack.addLast(UnaryMinusAst.builder().operand(op).build());
  }

  @Override
  public void enterIntLiteral(IntLiteralContext ctx) {
    IntConstantAst expr = IntConstantAst.builder()
        .literal(ctx.getText())
        .characterStart(ctx.getStart().getCharPositionInLine())
        .endLine(ctx.getStop().getLine())
        .startLine(ctx.getStart().getLine())
        .build();
    expressionStack.addLast(expr);
  }

  @Override
  public void enterAtom(AtomContext ctx) {
    Atom value = new Atom(ctx.IDENTIFIER().getText());
    AtomAst expr = AtomAst.builder()
        .atom(value)
        .characterStart(ctx.getStart().getCharPositionInLine())
        .endLine(ctx.getStop().getLine())
        .startLine(ctx.getStart().getLine())
        .build();
    expressionStack.addLast(expr);
  }

  @Override
  public void enterIdentifier(IdentifierContext ctx) {
    String name = ctx.getText();
    VariableAst typedVarAst = VariableAst.builder()
        .name(name)
        .characterStart(ctx.getStart().getCharPositionInLine())
        .endLine(ctx.getStop().getLine())
        .startLine(ctx.getStart().getLine())
        .build();
    expressionStack.addLast(typedVarAst);
  }
}
