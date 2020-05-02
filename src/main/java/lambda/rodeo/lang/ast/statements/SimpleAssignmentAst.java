package lambda.rodeo.lang.ast.statements;

import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.types.TypeScope;
import lambda.rodeo.lang.typed.statements.TypedAssignment;
import lambda.rodeo.lang.typed.statements.TypedSimpleAssignment;
import lambda.rodeo.lang.types.Type;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SimpleAssignmentAst implements AssigmentAst {

  private final String identifier;

  @Override
  public TypeScope scopeAfter(TypeScope scopeBefore, CompileContext compileContext, Type type) {
    return scopeBefore.declare(identifier, type);
  }

  @Override
  public TypedAssignment toTypedAssignmentAst(TypeScope after) {
    return TypedSimpleAssignment.builder()
        .assignmentAst(this)
        .typeScope(after)
        .build();
  }

}
