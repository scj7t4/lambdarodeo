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
import lambda.rodeo.lang.types.Type;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

//TODO: TEST
public class FunctionBodyAstFactory extends LambdaRodeoBaseListener {
  private final List<StatementAst> statements = new ArrayList<>();
  private final CompileContext compileContext;
  private TypeScope typeScope;

  public FunctionBodyAstFactory(
      FunctionBodyContext ctx,
      FunctionSigAst functionSigAst,
      CompileContext compileContext) {
    this.compileContext = compileContext;
    typeScope = functionSigAst.getInitialTypeScope();
    ParseTreeWalker.DEFAULT.walk(this, ctx);
  }

  FunctionBodyAst toAst() {
    StatementAst statementAst = statements.get(statements.size() - 1);
    if(statementAst.getAssignment() != null) {
      compileContext.getCompileErrorCollector().collect(
          CompileError.builder().build());
    }
    return FunctionBodyAst.builder()
        .finalTypeScope(this.typeScope)
        .statements(statements)
        .build();
  }

  @Override
  public void enterStatement(StatementContext ctx) {
    StatementAstFactory statementAstFactory = new StatementAstFactory(ctx, typeScope,
        compileContext);
    StatementAst statementAst = statementAstFactory.toAst();
    this.typeScope = statementAst.typeScope(this.typeScope);
    statements.add(statementAst);
  }
}
