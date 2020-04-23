package lambda.rodeo.lang.functions;

import lambda.rodeo.lang.exceptions.CriticalLanguageException;
import lambda.rodeo.lang.expressions.ExpressionAst;
import lambda.rodeo.lang.statements.Scope;
import lambda.rodeo.lang.statements.TypeScope;
import lambda.rodeo.lang.types.Type;
import lambda.rodeo.lang.values.Computable;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TypedVarAst implements ExpressionAst, Computable<Object> {

  private final String name;
  private final Type type;

  @Override
  public Type getType(TypeScope typeScope) {
    return type;
  }

  @Override
  public Computable<?> getComputable() {
    return this;
  }

  @Override
  public Object compute(Scope scope) {
    return scope.get(this.name)
        .orElseThrow(() -> new CriticalLanguageException("Function argument was not defined in"
            + "scope"));
  }
}
