package lambda.rodeo.lang.ast.statements;

import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.types.TypeScope;
import lambda.rodeo.lang.typed.statements.TypedAssignmentAst;
import lambda.rodeo.lang.typed.statements.TypedSimpleAssignmentAst;
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
  public TypedAssignmentAst toTypedAssignmentAst(TypeScope after) {
    return TypedSimpleAssignmentAst.builder()
        .assignmentAst(this)
        .typeScope(after)
        .build();
  }

}
