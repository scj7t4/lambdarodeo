package lambda.rodeo.lang.ast.functions;

import java.util.ArrayList;
import java.util.List;
import lambda.rodeo.lang.antlr.LambdaRodeoBaseListener;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.FunctionBodyContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.StatementContext;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.ast.statements.StatementAst;
import lambda.rodeo.lang.ast.statements.StatementAstFactory;
import lambda.rodeo.lang.types.TypeScope;
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
      // TODO: Compiler error here
    }
    return FunctionBodyAst.builder()
        .statements(statements)
        .build();
  }

  @Override
  public void enterStatement(StatementContext ctx) {
    StatementAstFactory statementAstFactory = new StatementAstFactory(ctx
    );
    StatementAst statementAst = statementAstFactory.toAst();
    statements.add(statementAst);
  }


}
