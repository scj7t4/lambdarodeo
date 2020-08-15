package lambda.rodeo.lang.s1ast.functions;

import lambda.rodeo.lang.antlr.LambdaRodeoBaseListener;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.FunctionBodyContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.FunctionDefContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.FunctionSigContext;
import lambda.rodeo.lang.compilation.S1CompileContext;
import lambda.rodeo.lang.s1ast.functions.FunctionAst.FunctionAstBuilder;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class FunctionAstFactory extends LambdaRodeoBaseListener {

  private FunctionAstBuilder builder = FunctionAst.builder();
  private final S1CompileContext compileContext;
  private FunctionSigAst functionSigAst;
  private FunctionBodyAst functionBodyAst;

  public FunctionAstFactory(
      FunctionDefContext ctx,
      S1CompileContext compileContext) {
    this.compileContext = compileContext;
    builder = builder.startLine(ctx.getStart().getLine())
        .endLine(ctx.getStop().getLine())
        .characterStart(ctx.getStart().getCharPositionInLine());
    ParseTreeWalker.DEFAULT.walk(this, ctx);
  }

  @Override
  public void enterFunctionSig(FunctionSigContext ctx) {
    FunctionSigAstFactory functionSigAstFactory = new FunctionSigAstFactory(ctx, compileContext);
    this.functionSigAst = functionSigAstFactory.toAst();
  }

  @Override
  public void enterFunctionBody(FunctionBodyContext ctx) {
    this.functionBodyAst = new FunctionBodyAstFactory(ctx, compileContext)
        .toAst();

    this.functionBodyAst.checkForLastStatementAssignment(compileContext);
  }

  public FunctionAst toAst() {
    builder = builder
        .functionSignature(functionSigAst)
        .functionBodyAst(functionBodyAst);
    return builder.build();
  }

}
