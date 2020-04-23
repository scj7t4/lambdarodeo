package lambda.rodeo.lang.functions;

import java.util.ArrayList;
import java.util.List;
import lambda.rodeo.lang.antlr.LambdaRodeoBaseListener;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.FunctionBodyContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.StatementContext;
import lambda.rodeo.lang.statements.StatementAst;
import lambda.rodeo.lang.statements.StatementAstFactory;
import lambda.rodeo.lang.statements.TypeScope;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

//TODO: TEST
public class FunctionBodyAstFactory extends LambdaRodeoBaseListener {
  private final List<StatementAst> statements = new ArrayList<>();
  private TypeScope typeScope;

  public FunctionBodyAstFactory(FunctionBodyContext ctx, TypeScope typeScope) {
    ParseTreeWalker.DEFAULT.walk(this, ctx);
    this.typeScope = typeScope;
  }

  FunctionBodyAst toAst() {
    return FunctionBodyAst.builder()
        .statements(statements)
        .build();
  }

  @Override
  public void enterStatement(StatementContext ctx) {
    StatementAstFactory statementAstFactory = new StatementAstFactory(ctx, typeScope);
    StatementAst statementAst = statementAstFactory.toAst();
    this.typeScope = statementAst.typeScope(this.typeScope);
    statements.add(statementAst);
  }
}
