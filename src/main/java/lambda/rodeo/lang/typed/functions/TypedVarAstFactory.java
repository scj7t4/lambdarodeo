package lambda.rodeo.lang.typed.functions;

import lambda.rodeo.lang.antlr.LambdaRodeoBaseListener;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.TypeExpressionContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.TypedVarContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.VarNameContext;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class TypedVarAstFactory extends LambdaRodeoBaseListener {

  private TypedVarAst.TypedVarAstBuilder builder = TypedVarAst.builder();

  public TypedVarAstFactory(TypedVarContext ctx) {
    ParseTreeWalker.DEFAULT.walk(this, ctx);
  }

  public TypedVarAst toAst() {
    return builder.build();
  }

  @Override
  public void enterVarName(VarNameContext ctx) {
    builder = builder.name(ctx.getText());
  }

  @Override
  public void enterTypeExpression(TypeExpressionContext ctx) {
    TypeFactory typeFactory = new TypeFactory(ctx);
    builder.type(typeFactory.toAst());
  }
}
