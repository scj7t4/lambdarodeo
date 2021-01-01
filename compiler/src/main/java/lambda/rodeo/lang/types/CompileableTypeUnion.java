package lambda.rodeo.lang.types;

import static org.objectweb.asm.Opcodes.AASTORE;
import static org.objectweb.asm.Opcodes.ANEWARRAY;
import static org.objectweb.asm.Opcodes.BIPUSH;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;

import java.util.List;
import java.util.stream.Collectors;
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
    List<String> descriptors = unions.stream()
        .map(CompileableType::getDescriptor)
        .distinct()
        .collect(Collectors.toList());
    if (descriptors.size() == 1) {
      return descriptors.get(0);
    }
    return Type.getDescriptor(Object.class);
  }

  @Override
  public String getInternalName() {
    List<String> internalNames = unions.stream()
        .map(CompileableType::getInternalName)
        .distinct()
        .collect(Collectors.toList());
    if (internalNames.size() == 1) {
      return internalNames.get(0);
    }
    return Type.getInternalName(Object.class);
  }

  @Override
  public String getSignature() {
    List<String> signatures = unions.stream()
        .map(CompileableType::getSignature)
        .distinct()
        .collect(Collectors.toList());
    if (signatures.size() == 1) {
      return signatures.get(0);
    }
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

    methodVisitor.visitTypeInsn(ANEWARRAY, Type.getInternalName(LRType.class));
    for (int i = 0; i < unions.size(); i++) {
      methodVisitor.visitInsn(DUP);
      methodVisitor.visitIntInsn(BIPUSH, unions.size());
      unions.get(i).provideRuntimeType(methodVisitor);
      methodVisitor.visitInsn(AASTORE);
    }
    methodVisitor.visitMethodInsn(INVOKESPECIAL,
        Type.getInternalName(LRTypeUnion.class),
        "<init>",
        "([Llambda/rodeo/runtime/types/LRType;)V",
        false);

  }
}