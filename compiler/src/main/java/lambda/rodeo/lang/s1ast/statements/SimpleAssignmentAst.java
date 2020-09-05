package lambda.rodeo.lang.s1ast.statements;

import java.util.Set;
import lambda.rodeo.lang.compilation.CompileError;
import lambda.rodeo.lang.s1ast.functions.ToTypedFunctionContext;
import lambda.rodeo.lang.scope.TypeScope;
import lambda.rodeo.lang.s2typed.statements.TypedAssignment;
import lambda.rodeo.lang.s2typed.statements.TypedSimpleAssignment;
import lambda.rodeo.lang.scope.TypedModuleScope;
import lambda.rodeo.runtime.types.CompileableType;
import lambda.rodeo.runtime.types.LambdaRodeoType;
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
  public TypeScope scopeAfter(TypeScope scopeBefore, ToTypedFunctionContext compileContext, CompileableType type) {
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

  @Override
  public void checkCollisionAgainstModule(TypedModuleScope typedModuleScope,
      ToTypedFunctionContext compileContext) {
    if(typedModuleScope.nameExists(identifier)) {
      compileContext.getCompileErrorCollector().collect(
          CompileError.identifierAlreadyDeclaredInScope(this, identifier)
      );
    }
  }

}
