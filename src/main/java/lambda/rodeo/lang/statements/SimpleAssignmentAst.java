package lambda.rodeo.lang.statements;

import static org.objectweb.asm.Opcodes.ASTORE;

import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.exceptions.CriticalLanguageException;
import lambda.rodeo.lang.statements.TypeScope.Entry;
import lambda.rodeo.lang.types.Type;
import lombok.Builder;
import lombok.Getter;
import org.objectweb.asm.MethodVisitor;

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
