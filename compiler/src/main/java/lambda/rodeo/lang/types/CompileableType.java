package lambda.rodeo.lang.types;

import lambda.rodeo.lang.util.DescriptorUtils;
import lambda.rodeo.runtime.lambda.Lambda0;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

public interface CompileableType {
  LambdaRodeoType getType();

  boolean isLambda();

  String getDescriptor();

  default String getLambdaDescriptor() {
    return Type.getDescriptor(Lambda0.class);
  }

  String getInternalName();

  default String getSignature() {
    return DescriptorUtils.genericType(Lambda0.class, getDescriptor());
  }

  default boolean assignableFrom(CompileableType other) {
    return this.equals(other);
  }

  default boolean allocateSlot() {
    return true;
  }

  public void provideRuntimeType(MethodVisitor methodVisitor);
}
