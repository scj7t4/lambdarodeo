package lambda.rodeo.runtime.types;

public interface Type {

  Class<?> javaType();

  default boolean allocateSlot() {
    return true;
  }

  CompileableType toCompileableType();
}
