package lambda.rodeo.lang.functions;

import java.util.List;
import lambda.rodeo.lang.statements.Scope;
import lambda.rodeo.lang.statements.StatementAst;
import lambda.rodeo.lang.statements.TypedValue;
import lambda.rodeo.lang.types.Type;
import lombok.Builder;

@Builder
public class FunctionBodyAst {
  private final List<StatementAst> statements;

  public TypedValue compute(Scope initScope) {
    Scope current = initScope;
    for(StatementAst statement : statements) {
      current = statement.compute(current);
    }
    return current.get("$last").orElse(null); //TODO this should throw or something
  }

  public Type getType() {
    return statements.get(statements.size() - 1).getType();
  }
}
