package lambda.rodeo.lang.scope;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ASTORE;

import lambda.rodeo.lang.scope.CompileableTypeScope.CompileableEntry;
import lambda.rodeo.lang.types.CompileableType;
import lombok.Builder;
import lombok.Getter;
import org.objectweb.asm.MethodVisitor;

@Builder
@Getter
public class SimpleEntry implements Entry {

  private final String name;
  private final CompileableType type;
  private final int index;

  public CompileableEntry toCompileableEntry() {
    return CompileableEntry.builder()
        .index(index)
        .name(name)
        .type(type)
        .build();
  }

  @Override
  public void compileLoad(MethodVisitor methodVisitor) {
    methodVisitor.visitVarInsn(ALOAD, getIndex());
  }

  @Override
  public void compileStore(MethodVisitor methodVisitor) {
    methodVisitor.visitVarInsn(ASTORE, getIndex());
  }
}
