package lambda.rodeo.lang.s1ast.functions;

import lambda.rodeo.lang.antlr.LambdaRodeoBaseListener;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.TypeExpressionContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.TypedVarContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.VarNameContext;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class TypedVarFactory extends LambdaRodeoBaseListener {

  private TypedVar.TypedVarBuilder builder = TypedVar.builder();

  public TypedVarFactory(TypedVarContext ctx) {
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
    TypeFactory typeFactory = new TypeFactory(ctx);
    builder.type(typeFactory.toAst());
  }
}
