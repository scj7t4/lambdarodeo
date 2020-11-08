package lambda.rodeo.lang.s1ast.type;

import lambda.rodeo.lang.antlr.LambdaRodeoBaseVisitor;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.LambdaTypedVarContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.TypedVarContext;

public class TypedVarFactory extends LambdaRodeoBaseVisitor<TypedVar> {

  private TypedVar ast;

  public TypedVarFactory(TypedVarContext ctx) {
    ast = visit(ctx);
  }

  public TypedVarFactory(LambdaTypedVarContext ctx) {
    ast = visit(ctx);
  }

  public TypedVar toAst() {
    return ast;
  }

  @Override
  public TypedVar visitTypedVar(TypedVarContext ctx) {
    TypeExpressionFactory typeExpressionFactory
        = new TypeExpressionFactory(ctx.varType().typeExpression());

    return TypedVar.builder()
        .startLine(ctx.getStart().getLine())
        .endLine(ctx.getStop().getLine())
        .characterStart(ctx.getStart().getCharPositionInLine())
        .name(ctx.varName().getText())
        .type(typeExpressionFactory.toAst())
        .build();
  }

  @Override
  public TypedVar visitLambdaTypedVar(LambdaTypedVarContext ctx) {
    TypeExpressionFactory typeExpressionFactory
        = new TypeExpressionFactory(ctx.varType().typeExpression());

    return TypedVar.builder()
        .startLine(ctx.getStart().getLine())
        .endLine(ctx.getStop().getLine())
        .characterStart(ctx.getStart().getCharPositionInLine())
        .name(ctx.varName().getText())
        .type(typeExpressionFactory.toAst())
        .build();
  }
}
