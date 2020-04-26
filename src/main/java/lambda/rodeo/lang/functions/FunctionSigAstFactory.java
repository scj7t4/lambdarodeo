package lambda.rodeo.lang.functions;

import java.util.ArrayList;
import java.util.List;
import lambda.rodeo.lang.antlr.LambdaRodeoBaseListener;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.FunctionArgsContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.FunctionNameContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.FunctionSigContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.ReturnTypeContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.TypeExpressionContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.TypedVarContext;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.types.Type;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class FunctionSigAstFactory extends LambdaRodeoBaseListener {

  private final CompileContext compileContext;
  private FunctionSigAst.FunctionSigAstBuilder builder = FunctionSigAst.builder();
  private final List<TypedVarAst> arguments = new ArrayList<>();
  private Type returnType;

  public FunctionSigAstFactory(FunctionSigContext ctx, CompileContext compileContext) {
    this.compileContext = compileContext;
    builder.arguments(arguments);
    ParseTreeWalker.DEFAULT.walk(this, ctx);
  }

  @Override
  public void enterFunctionSig(FunctionSigContext ctx) {
    builder.startLine(ctx.getStart().getLine());
    builder.endLine(ctx.getStop().getLine());
    builder.characterStart(ctx.getStart().getLine());
  }

  @Override
  public void enterFunctionName(FunctionNameContext ctx) {
    builder = builder.name(ctx.getText());
  }


  @Override
  public void enterTypedVar(TypedVarContext ctx) {
    arguments.add(new TypedVarAstFactory(ctx).toAst());
  }

  @Override
  public void exitFunctionArgs(FunctionArgsContext ctx) {
    // TODO: Check for variables already declared.
  }

  @Override
  public void enterReturnType(ReturnTypeContext ctx) {
    TypeExpressionContext typeExpressionContext = ctx.typeExpression();
    returnType = new TypeFactory(typeExpressionContext).toAst();
  }

  public FunctionSigAst toAst() {
    return builder
        .declaredReturnType(returnType)
        .build();
  }
}
