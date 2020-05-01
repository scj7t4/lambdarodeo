package lambda.rodeo.lang.types;

public interface Type {

  Class<?> javaType();

  default boolean allocateSlot() {
    return true;
  }

  default String descriptor() {
    return org.objectweb.asm.Type.getDescriptor(javaType());
  }
}
