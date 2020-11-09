package lambda.rodeo.lang.s1ast.type;

import lambda.rodeo.lang.antlr.LambdaRodeoBaseVisitor;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.LambdaTypedVarContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.TypedVarContext;
import lambda.rodeo.lang.compilation.CollectsErrors;

public class TypedVarFactory extends LambdaRodeoBaseVisitor<TypedVar> {

  private final TypedVar ast;
  private final CollectsErrors compileContext;

  public TypedVarFactory(TypedVarContext ctx,
      CollectsErrors compileContext) {
    ast = visit(ctx);
    this.compileContext = compileContext;
  }

  public TypedVarFactory(LambdaTypedVarContext ctx,
      CollectsErrors compileContext) {
    ast = visit(ctx);
    this.compileContext = compileContext;
  }

  public TypedVar toAst() {
    return ast;
  }

  @Override
  public TypedVar visitTypedVar(TypedVarContext ctx) {
    TypeExpressionFactory typeExpressionFactory
        = new TypeExpressionFactory(ctx.varType().typeExpression(), compileContext);

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
        = new TypeExpressionFactory(ctx.varType().typeExpression(), compileContext);

    return TypedVar.builder()
        .startLine(ctx.getStart().getLine())
        .endLine(ctx.getStop().getLine())
        .characterStart(ctx.getStart().getCharPositionInLine())
        .name(ctx.varName().getText())
        .type(typeExpressionFactory.toAst())
        .build();
  }
}
