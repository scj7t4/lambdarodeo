package lambda.rodeo.lang.types;


import static org.objectweb.asm.Opcodes.GETSTATIC;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lambda.rodeo.lang.compilation.CollectsErrors;
import lambda.rodeo.lang.scope.Entry;
import lambda.rodeo.lang.scope.TypedModuleScope;
import lambda.rodeo.runtime.types.LRString;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

public class StringType implements LambdaRodeoType, CompileableType {
  public static final StringType INSTANCE = new StringType();

  @Override
  public String getDescriptor() {
    return Type.getDescriptor(String.class);
  }

  @Override
  public String getInternalName() {
    return Type.getInternalName(String.class);
  }

  @Override
  public void provideRuntimeType(MethodVisitor methodVisitor) {
    methodVisitor.visitFieldInsn(GETSTATIC,
        Type.getInternalName(LRString.class),
        "INSTANCE",
        Type.getDescriptor(LRString.class));
  }

  @Override
  public Optional<Entry> getMemberEntry(Entry parent, String name) {
    return Optional.empty();
  }

  @Override
  public CompileableType toCompileableType(
      TypedModuleScope typedModuleScope,
      CollectsErrors compileContext) {
    return this;
  }

  @Override
  public LambdaRodeoType getType() {
    return this;
  }

  public String toString() {
    return "String";
  }
}
