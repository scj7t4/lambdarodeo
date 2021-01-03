package lambda.rodeo.lang.types;

import static org.objectweb.asm.Opcodes.AASTORE;
import static org.objectweb.asm.Opcodes.ANEWARRAY;
import static org.objectweb.asm.Opcodes.BIPUSH;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lambda.rodeo.lang.compilation.CollectsErrors;
import lambda.rodeo.lang.compilation.CompileError;
import lambda.rodeo.lang.scope.Entry;
import lambda.rodeo.runtime.types.LRType;
import lambda.rodeo.runtime.types.LRTypeUnion;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

@Builder
@EqualsAndHashCode
public class CompileableTypeUnion implements CompileableType {

  @NonNull
  private final Set<CompileableType> unions;
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
    if (Objects.equals(this, other)) {
      return true;
    }
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
    Iterator<CompileableType> it = unions.iterator();
    while(it.hasNext()) {
      methodVisitor.visitInsn(DUP);
      methodVisitor.visitIntInsn(BIPUSH, unions.size());
      it.next().provideRuntimeType(methodVisitor);
      methodVisitor.visitInsn(AASTORE);
    }
    methodVisitor.visitMethodInsn(INVOKESPECIAL,
        Type.getInternalName(LRTypeUnion.class),
        "<init>",
        "([Llambda/rodeo/runtime/types/LRType;)V",
        false);

  }

  @Override
  public Optional<Entry> getMemberEntry(Entry parent, String name) {
    List<Entry> mergeable = unions.stream()
        .map(type -> type.getMemberEntry(parent, name))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(Collectors.toList());
    if (mergeable.isEmpty()) {
      return Optional.empty();
    }
    // TODO: if members get added to non-LRObjects this won't work...
    Set<CompileableType> mergedTypes = mergeable.stream()
        .map(Entry::getType)
        .collect(Collectors.toSet());
    Set<LambdaRodeoType> mergedTypeSimple = mergedTypes.stream()
        .map(CompileableType::getType)
        .collect(Collectors.toSet());
    CompileableTypeUnion mergedType = CompileableTypeUnion.builder()
        .unions(mergedTypes)
        .type(TypeUnion.builder().unions(mergedTypeSimple).build())
        .build();
    LRInterfaceEntry merged = LRInterfaceEntry.builder()
        .name(name)
        .type(mergedType)
        .build();
    return Optional.of(merged);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("(");
    for (CompileableType type : unions) {
      sb.append(type.toString());
      sb.append(" | ");
    }
    return sb.substring(0, sb.length() - 3) + ")";
  }
}
