package lambda.rodeo.lang.statements;

import lambda.rodeo.lang.types.Type;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SimpleAssignmentAst {

  private final String identifier;

  public Scope assign(Scope scopeBefore, Object value) {
    return scopeBefore.put(identifier, value);
  }

  public TypeScope type(TypeScope scopeBefore, Type type) {
    return scopeBefore.put(identifier, type);
  }
}
