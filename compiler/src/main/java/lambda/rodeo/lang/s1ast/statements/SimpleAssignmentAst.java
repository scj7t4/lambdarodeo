package lambda.rodeo.lang.s1ast.statements;

import java.util.Collections;
import java.util.Set;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.compilation.CompileError;
import lambda.rodeo.lang.scope.TypeScope;
import lambda.rodeo.lang.s2typed.statements.TypedAssignment;
import lambda.rodeo.lang.s2typed.statements.TypedSimpleAssignment;
import lambda.rodeo.lang.scope.TypeScopeImpl;
import lambda.rodeo.runtime.types.Type;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class SimpleAssignmentAst implements AssigmentAst {

  private final String identifier;
  private final int startLine;
  private final int endLine;
  private final int characterStart;

  @Override
  public TypeScope scopeAfter(TypeScope scopeBefore, CompileContext compileContext, Type type) {
    boolean alreadyDeclared = scopeBefore.get(identifier).findAny().isPresent();
    if(alreadyDeclared) {
      compileContext.getCompileErrorCollector().collect(
          CompileError.identifierAlreadyDeclaredInScope(this, identifier)
      );
      return scopeBefore;
    }
    return scopeBefore.declare(identifier, type);
  }

  @Override
  public TypedAssignment toTypedAssignmentAst(TypeScope after) {
    return TypedSimpleAssignment.builder()
        .assignmentAst(this)
        .typeScope(after)
        .build();
  }

  @Override
  public Set<String> newDeclarations() {
    return Set.of(identifier);
  }

}
