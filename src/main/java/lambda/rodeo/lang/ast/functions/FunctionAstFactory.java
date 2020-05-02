package lambda.rodeo.lang.ast.functions;

import java.util.ArrayList;
import java.util.List;
import lambda.rodeo.lang.antlr.LambdaRodeoBaseListener;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.FunctionBodyContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.FunctionDefContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.FunctionSigContext;
import lambda.rodeo.lang.ast.functions.FunctionAst.FunctionAstBuilder;
import lambda.rodeo.lang.ast.statements.StatementAst;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.compilation.CompileError;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class FunctionAstFactory extends LambdaRodeoBaseListener {

  private final List<TypedVar> arguments = new ArrayList<>();
  private FunctionAstBuilder builder = FunctionAst.builder();
  private final CompileContext compileContext;
  private FunctionSigAst functionSigAst;
  private FunctionBodyAst functionBodyAst;

  public FunctionAstFactory(
      FunctionDefContext ctx,
      CompileContext compileContext) {
    this.compileContext = compileContext;
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
    List<StatementAst> statements = functionBodyAst.getStatements();
    StatementAst lastStatement = statements.get(statements.size() - 1);
    if (lastStatement.getAssignment() != null) {
      compileContext.getCompileErrorCollector().collect(
          CompileError.lastStatementCannotBeAssignment(lastStatement)
      );
    }
  }

  public FunctionAst toAst() {
    builder = builder
        .functionSignature(functionSigAst)
        .functionBodyAst(functionBodyAst);
    return builder.build();
  }

}
