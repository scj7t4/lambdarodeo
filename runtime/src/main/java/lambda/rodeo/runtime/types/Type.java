package lambda.rodeo.runtime.types;

public interface Type {

  String getDescriptor();

  default boolean allocateSlot() {
    return true;
  }

  CompileableType toCompileableType();

  default boolean assignableFrom(Type other) {
    return this.equals(other);
  }
}
