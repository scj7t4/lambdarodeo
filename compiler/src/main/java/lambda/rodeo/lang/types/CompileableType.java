package lambda.rodeo.lang.types;

import org.objectweb.asm.MethodVisitor;

public interface CompileableType {
  LambdaRodeoType getType();

  boolean isLambda();
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

  public void provideRuntimeType(MethodVisitor methodVisitor);
}
