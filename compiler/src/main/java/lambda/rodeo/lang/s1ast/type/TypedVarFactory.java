package lambda.rodeo.lang.s1ast.type;

import lambda.rodeo.lang.antlr.LambdaRodeoBaseListener;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.LambdaTypedVarContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.TypeExpressionContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.TypedVarContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.VarNameContext;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class TypedVarFactory extends LambdaRodeoBaseListener {

  private TypedVar.TypedVarBuilder builder;

  public TypedVarFactory(TypedVarContext ctx) {
    builder = TypedVar.builder()
        .startLine(ctx.getStart().getLine())
        .endLine(ctx.getStop().getLine())
        .characterStart(ctx.getStart().getCharPositionInLine());
    ParseTreeWalker.DEFAULT.walk(this, ctx);
  }

  public TypedVarFactory(LambdaTypedVarContext ctx) {
    builder = TypedVar.builder()
        .startLine(ctx.getStart().getLine())
        .endLine(ctx.getStop().getLine())
        .characterStart(ctx.getStart().getCharPositionInLine());
    ParseTreeWalker.DEFAULT.walk(this, ctx);
  }

  public TypedVar toAst() {
    return builder.build();
  }

  @Override
  public void enterVarName(VarNameContext ctx) {
    builder = builder.name(ctx.getText());
  }

  @Override
  public void enterTypeExpression(TypeExpressionContext ctx) {
    TypeExpressionFactory typeExpressionFactory = new TypeExpressionFactory(ctx);
    builder.type(typeExpressionFactory.toAst());
  }
}
