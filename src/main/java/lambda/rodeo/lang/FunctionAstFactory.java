package lambda.rodeo.lang;

import java.util.ArrayList;
import java.util.List;
import lambda.rodeo.lang.FunctionAst.FunctionAstBuilder;
import lambda.rodeo.lang.antlr.LambdaRodeoBaseListener;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.ExprContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.FunctionDefContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.FunctionNameContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.TypedVarContext;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class FunctionAstFactory extends LambdaRodeoBaseListener {

  private final List<TypedVarAst> arguments = new ArrayList<>();
  private FunctionAstBuilder builder = FunctionAst.builder();

  public FunctionAstFactory(FunctionDefContext ctx) {
    ParseTreeWalker.DEFAULT.walk(this, ctx);
  }

  @Override
  public void enterFunctionName(FunctionNameContext ctx) {
    builder = builder.name(ctx.getText());
  }


  @Override
  public void enterTypedVar(TypedVarContext ctx) {
    arguments.add(new TypedVarAstFactory(ctx).toAst());
  }

  public FunctionAst toAst() {
    builder.arguments(arguments);
    return builder.build();
  }

}
