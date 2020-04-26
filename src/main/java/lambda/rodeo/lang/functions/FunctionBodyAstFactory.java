package lambda.rodeo.lang.functions;

import java.util.ArrayList;
import java.util.List;
import lambda.rodeo.lang.antlr.LambdaRodeoBaseListener;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.FunctionBodyContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.StatementContext;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.compilation.CompileError;
import lambda.rodeo.lang.statements.StatementAst;
import lambda.rodeo.lang.statements.StatementAstFactory;
import lambda.rodeo.lang.statements.TypeScope;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

//TODO: TEST
public class FunctionBodyAstFactory extends LambdaRodeoBaseListener {

  private final List<StatementAst> statements = new ArrayList<>();
  private final CompileContext compileContext;
  private TypeScope initialTypeScope;

  public FunctionBodyAstFactory(
      FunctionBodyContext ctx,
      FunctionSigAst functionSigAst,
      CompileContext compileContext) {
    this.compileContext = compileContext;
    initialTypeScope = functionSigAst.getInitialTypeScope();
    ParseTreeWalker.DEFAULT.walk(this, ctx);
  }

  FunctionBodyAst toAst() {
    StatementAst statementAst = statements.get(statements.size() - 1);
    if (statementAst.getAssignment() != null) {
      compileContext.getCompileErrorCollector().collect(
          CompileError.builder().build());
    }
    return FunctionBodyAst.of(statements, initialTypeScope);
  }

  @Override
  public void enterStatement(StatementContext ctx) {
    StatementAstFactory statementAstFactory = new StatementAstFactory(ctx, initialTypeScope,
        compileContext);
    StatementAst statementAst = statementAstFactory.toAst();
    statements.add(statementAst);
  }


}
