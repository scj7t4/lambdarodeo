package lambda.rodeo.lang.types;

import static org.objectweb.asm.Opcodes.INVOKESTATIC;

import java.util.List;
import lambda.rodeo.lang.util.FunctionDescriptorBuilder;
import lambda.rodeo.runtime.types.LRType;
import lambda.rodeo.runtime.types.LRTypeUnion;
import lombok.Builder;
import lombok.NonNull;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

@Builder
public class CompileableTypeUnion implements CompileableType {
  @NonNull
  private final List<CompileableType> unions;
  @NonNull
  private final LambdaRodeoType type;

  @Override
  public LambdaRodeoType getType() {
    return type;
  }

  @Override
  public String getDescriptor() {
    return Type.getDescriptor(Object.class);
  }

  @Override
  public String getInternalName() {
    return Type.getInternalName(Object.class);
  }

  @Override
  public String getSignature() {
    return Type.getDescriptor(Object.class);
  }

  @Override
  public boolean assignableFrom(CompileableType other) {
    return unions.stream()
        .anyMatch(union -> union.assignableFrom(other));
  }

  @Override
  public boolean allocateSlot() {
    return true;
  }

  @Override
  public void provideRuntimeType(MethodVisitor methodVisitor) {
    unions.forEach(union -> provideRuntimeType(methodVisitor));
    methodVisitor.visitMethodInsn(INVOKESTATIC,
        Type.getInternalName(LRTypeUnion.class), "make",
        FunctionDescriptorBuilder.args(LRType.class, LRType.class)
          .returns(LRTypeUnion.class),
        false);
  }
}
