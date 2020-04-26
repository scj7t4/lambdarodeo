package lambda.rodeo.lang.statements;

import static org.objectweb.asm.Opcodes.ASTORE;

import lambda.rodeo.lang.exceptions.CriticalLanguageException;
import lambda.rodeo.lang.types.Type;
import lombok.Builder;
import lombok.Getter;
import org.objectweb.asm.MethodVisitor;

@Builder
@Getter
public class SimpleAssignmentAst {

  private final String identifier;

  public TypeScope scopeAfter(TypeScope scopeBefore, Type type) {
    return scopeBefore.declare(identifier, type);
  }
}
