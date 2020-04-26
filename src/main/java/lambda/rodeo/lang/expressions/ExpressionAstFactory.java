package lambda.rodeo.lang.expressions;

import static lambda.rodeo.lang.types.Atom.UNDEFINED_VAR;

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
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.compilation.CompileError;
import lambda.rodeo.lang.functions.TypedVarAst;
import lambda.rodeo.lang.statements.TypeScope;
import lambda.rodeo.lang.statements.TypeScope.Entry;
import lambda.rodeo.lang.types.Atom;
import lambda.rodeo.lang.types.Type;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

@Slf4j
public class ExpressionAstFactory extends LambdaRodeoBaseListener {

  private Deque<ExpressionAst> expressionStack = new LinkedList<>();
  private final TypeScope typeScope;
  private final CompileContext compileContext;

  public ExpressionAstFactory(ExprContext ctx,
      TypeScope typeScope,
      CompileContext compileContext) {
    this.typeScope = typeScope;
    this.compileContext = compileContext;
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
      expressionStack.addLast(new AddAst(lhs, rhs, typeScope, compileContext));
    } else if ("-".equals(op)) {
      expressionStack.addLast(new SubtractAst(lhs, rhs, typeScope, compileContext));
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
      expressionStack.addLast(new MultiplyAst(lhs, rhs, typeScope, compileContext));
    } else if ("/".equals(op)) {
      expressionStack.addLast(new DivisionAst(lhs, rhs, typeScope, compileContext));
    } else {
      throw new UnsupportedOperationException(
          "Unrecognized multiply/divide operation '" + op + "'");
    }
  }

  @Override
  public void exitUnaryMinus(UnaryMinusContext ctx) {
    ExpressionAst op = expressionStack.pollLast();
    expressionStack.addLast(new UnaryMinusAst(op, typeScope, compileContext));
  }

  @Override
  public void enterIntLiteral(IntLiteralContext ctx) {
    IntConstantAst expr = IntConstantAst.builder()
        .literal(ctx.getText())
        .build();
    expressionStack.addLast(expr);
  }

  @Override
  public void enterAtom(AtomContext ctx) {
    Atom value = new Atom(ctx.IDENTIFIER().getText());
    AtomAst expr = AtomAst.builder()
        .atom(value)
        .build();
    expressionStack.addLast(expr);
  }

  @Override
  public void enterIdentifier(IdentifierContext ctx) {
    String name = ctx.getText();
    Type type = typeScope.get(name)
        .map(Entry::getType)
        .orElse(UNDEFINED_VAR);

    if (type == UNDEFINED_VAR) {
      compileContext.getCompileErrorCollector()
          .collect(CompileError.undefinedVariableError(name, ctx));
      expressionStack.addLast(AtomAst.builder()
          .atom(UNDEFINED_VAR)
          .build());
    } else {
      TypedVarAst typedVarAst = TypedVarAst.builder()
          .name(name)
          .type(type)
          .build();
      expressionStack.addLast(typedVarAst);
    }
  }
}
