package lambda.rodeo.lang.statements;

import lambda.rodeo.lang.types.Type;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SimpleAssignmentAst {

  private final String identifier;

  public TypeScope type(TypeScope scopeBefore, Type type) {
    return scopeBefore.declare(identifier, type);
  }
}
