package lambda.rodeo.lang.s1ast.functions.patterns;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ASTORE;
import static org.objectweb.asm.Opcodes.CHECKCAST;

import lambda.rodeo.lang.types.CompileableType;
import lombok.Builder;
import lombok.Getter;
import org.objectweb.asm.MethodVisitor;

@Builder
@Getter
public class ScopeReplaceAndCasts {

  private final int oldSlot;
  private final int newSlot;
  private final CompileableType toCastTo;

  public void compileCast(MethodVisitor methodVisitor) {
    // Will the variable names cause problems??
    methodVisitor.visitVarInsn(ALOAD, oldSlot);
    methodVisitor.visitTypeInsn(CHECKCAST, toCastTo.getInternalName());
    methodVisitor.visitVarInsn(ASTORE, newSlot);
  }
}
