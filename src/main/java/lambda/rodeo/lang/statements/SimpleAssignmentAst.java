package lambda.rodeo.lang.statements;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SimpleAssignmentAst {

  private final String identifier;

  public Scope assign(Scope result, TypedValue typedValue) {
    return result.put(identifier, typedValue);
  }
}
