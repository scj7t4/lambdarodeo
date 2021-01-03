package lambda.rodeo.lang.types;

import static org.objectweb.asm.Opcodes.GETSTATIC;

import java.util.Optional;
import lambda.rodeo.lang.compilation.CollectsErrors;
import lambda.rodeo.lang.scope.Entry;
import lambda.rodeo.lang.scope.TypeResolver;
import lambda.rodeo.runtime.types.LRAny;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

public class AnyType implements LambdaRodeoType, CompileableType {

  public static final AnyType INSTANCE = new AnyType();

  private AnyType() {
  }

  @Override
  public LambdaRodeoType getType() {
    return this;
  }

  @Override
  public String getDescriptor() {
    return Type.getDescriptor(Object.class);
  }

  @Override
  public String getInternalName() {
    return Type.getInternalName(Object.class);
  }

  @Override
  public void provideRuntimeType(MethodVisitor methodVisitor) {
    methodVisitor.visitFieldInsn(GETSTATIC,
        Type.getInternalName(LRAny.class),
        "INSTANCE",
        Type.getDescriptor(LRAny.class));
  }

  @Override
  public Optional<Entry> getMemberEntry(Entry parent, String name) {
    return Optional.empty();
  }

  @Override
  public CompileableType toCompileableType(TypeResolver typeResolver,
      CollectsErrors compileContext) {
    return this;
  }

  @Override
  public boolean assignableFrom(CompileableType other) {
    return true;
  }
}
