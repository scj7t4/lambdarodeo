package lambda.rodeo.runtime.types;

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
}
