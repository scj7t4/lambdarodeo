package lambda.rodeo.lang.types;

import lambda.rodeo.lang.typed.TypedModule;

public interface Type {

  Class<?> javaType();

  default boolean allocateSlot() {
    return true;
  }

  default String descriptor() {
    return org.objectweb.asm.Type.getDescriptor(javaType());
  }

  CompileableType toCompileableType(TypedModule typedModule);
}
