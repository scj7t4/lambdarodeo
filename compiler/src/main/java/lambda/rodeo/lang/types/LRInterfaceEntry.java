package lambda.rodeo.lang.types;

import static org.objectweb.asm.Opcodes.CHECKCAST;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;

import lambda.rodeo.lang.exceptions.CriticalLanguageException;
import lambda.rodeo.lang.scope.Entry;
import lambda.rodeo.lang.util.FunctionDescriptorBuilder;
import lambda.rodeo.runtime.types.LRObject;
import lombok.Builder;
import lombok.Getter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

@Builder
@Getter
public class LRInterfaceEntry implements Entry {

  private final Entry parent;
  private final String name;
  private final CompileableType type;

  @Override
  public void compileLoad(MethodVisitor methodVisitor) {
    parent.compileLoad(methodVisitor);
    methodVisitor.visitLdcInsn(name);
    methodVisitor.visitMethodInsn(INVOKEVIRTUAL,
        Type.getInternalName(LRObject.class),
        "get",
        FunctionDescriptorBuilder.args(String.class)
            .returns(Object.class),
        false);
    methodVisitor.visitTypeInsn(CHECKCAST, type.getInternalName());
  }

  @Override
  public void compileStore(MethodVisitor methodVisitor) {
    throw new CriticalLanguageException("You cannot assign to members of LRObjects");
  }
}
