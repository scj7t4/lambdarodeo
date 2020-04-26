package lambda.rodeo.lang.statements;

import static org.objectweb.asm.Opcodes.ASTORE;

import lambda.rodeo.lang.types.Type;
import lombok.Builder;
import lombok.Getter;
import org.objectweb.asm.MethodVisitor;

@Builder
@Getter
public class AssigmentAst {

  private final int index;
  private final String identifier;
  private final Type type;

  public void compile(MethodVisitor methodVisitor) {
    methodVisitor.visitVarInsn(ASTORE, index);
  }
}
