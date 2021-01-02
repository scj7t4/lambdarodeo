package lambda.rodeo.lang.types;

import java.util.List;
import java.util.Optional;
import lambda.rodeo.lang.scope.Entry;
import org.objectweb.asm.MethodVisitor;

public interface CompileableType {
  LambdaRodeoType getType();

  String getDescriptor();

  String getInternalName();

  default String getSignature() {
    return getDescriptor();
  }

  default boolean assignableFrom(CompileableType other) {
    return this.equals(other);
  }

  default boolean allocateSlot() {
    return true;
  }

  void provideRuntimeType(MethodVisitor methodVisitor);

  Optional<Entry> getMemberEntry(Entry parent, String name);
}
