package lambda.rodeo.runtime.types;

public interface LambdaRodeoType {

  String getDescriptor();
  default String getSignature() {
    return getDescriptor();
  };

  default boolean allocateSlot() {
    return true;
  }

  CompileableType toCompileableType();

  default boolean assignableFrom(LambdaRodeoType other) {
    return this.equals(other);
  }
}
