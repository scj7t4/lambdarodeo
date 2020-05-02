package lambda.rodeo.lang.ast.functions;

import java.util.ArrayList;
import java.util.List;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.ast.statements.StatementAst;
import lambda.rodeo.lang.types.TypeScope;
import lambda.rodeo.lang.typed.statements.TypedStatement;
import lambda.rodeo.lang.typed.functions.TypedFunctionBody;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FunctionBodyAst {

  private final List<StatementAst> statements;

  public TypedFunctionBody toTypedFunctionBodyAst(
      TypeScope initialTypeScope,
      CompileContext compileContext) {
    TypeScope current = initialTypeScope;
    List<TypedStatement> typedStatements = new ArrayList<>();
    for(StatementAst statement : statements) {
      TypedStatement typedStatement = statement.toTypedStatementAst(current, compileContext);
      current = typedStatement.getAfterTypeScope();
      typedStatements.add(typedStatement);
    }

    return TypedFunctionBody.builder()
        .functionBodyAst(this)
        .initialTypeScope(initialTypeScope)
        .statements(typedStatements)
        .build();
  }
}
