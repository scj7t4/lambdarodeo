package lambda.rodeo.lang.expressions;

import java.math.BigInteger;
import java.util.Deque;
import java.util.LinkedList;
import lambda.rodeo.lang.antlr.LambdaRodeoBaseListener;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.AddSubContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.ExprContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.IdentifierContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.IntLiteralContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.MultiDivContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.UnaryMinusContext;
import lambda.rodeo.lang.functions.TypedVarAst;
import lambda.rodeo.lang.statements.TypeScope;
import lambda.rodeo.lang.types.Atom;
import lambda.rodeo.lang.types.IntType;
import lambda.rodeo.lang.values.Constant;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

@Slf4j
public class ExpressionAstFactory extends LambdaRodeoBaseListener {

  private Deque<ExpressionAst> expressionStack = new LinkedList<>();
  private final TypeScope typeScope;

  public ExpressionAstFactory(ExprContext ctx, TypeScope typeScope) {
    ParseTreeWalker.DEFAULT.walk(this, ctx);
    this.typeScope = typeScope;
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
      expressionStack.addLast(new AddAst(lhs, rhs));
    } else if ("-".equals(op)) {
      expressionStack.addLast(new SubtractAst(lhs, rhs));
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
      expressionStack.addLast(new MultiplyAst(lhs, rhs));
    } else if ("/".equals(op)) {
      expressionStack.addLast(new DivisionAst(lhs, rhs));
    } else {
      throw new UnsupportedOperationException(
          "Unrecognized multiply/divide operation '" + op + "'");
    }
  }

  @Override
  public void exitUnaryMinus(UnaryMinusContext ctx) {
    ExpressionAst op = expressionStack.pollLast();
    expressionStack.addLast(new UnaryMinusAst(op));
  }

  @Override
  public void enterIntLiteral(IntLiteralContext ctx) {
    BigInteger value = new BigInteger(ctx.getText());
    ConstantExpr<BigInteger> expr = ConstantExpr.<BigInteger>builder()
        .type(IntType.INSTANCE)
        .computable(Constant.<BigInteger>builder().value(value).build())
        .build();
    expressionStack.addLast(expr);
  }

  @Override
  public void enterIdentifier(IdentifierContext ctx) {
    String name = ctx.getText();
    TypedVarAst typedVarAst = TypedVarAst.builder()
        .name(name)
        .type(typeScope.get(name).orElse(new Atom("UNDEFINED_VAR")))
        .build();
    expressionStack.addLast(typedVarAst);
  }
}
