package lambda.rodeo.lang.ast.functions;

import java.util.ArrayList;
import java.util.List;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.ast.statements.StatementAst;
import lambda.rodeo.lang.ast.statements.TypeScope;
import lambda.rodeo.lang.ast.statements.TypedStatementAst;
import lambda.rodeo.lang.typed.functions.TypedFunctionBodyAst;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FunctionBodyAst {

  private final List<StatementAst> statements;

  public TypedFunctionBodyAst toTypedFunctionBodyAst(
      TypeScope initialTypeScope,
      CompileContext compileContext) {
    TypeScope current = initialTypeScope;
    List<TypedStatementAst> typedStatementAsts = new ArrayList<>();
    for(StatementAst statement : statements) {
      TypedStatementAst typedStatementAst = statement.toTypedStatementAst(current, compileContext);
      current = typedStatementAst.getAfterTypeScope();
      typedStatementAsts.add(typedStatementAst);
    }

    return TypedFunctionBodyAst.builder()
        .functionBodyAst(this)
        .initialTypeScope(initialTypeScope)
        .statements(typedStatementAsts)
        .build();
  }
}
