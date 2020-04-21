package lambda.rodeo.lang.statement;

import lambda.rodeo.lang.types.Type;

public class AssignmentAst {

  private final String identifier;

  public AssignmentAst(String identifier) {
    this.identifier = identifier;
  }

  public Scope assign(Scope result, Type type, Object computed) {
    return result.put(identifier, TypedValue.builder()
        .type(type)
        .value(computed)
        .build());
  }
}
