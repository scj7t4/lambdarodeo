package lambda.rodeo.lang.types;

import static org.objectweb.asm.Opcodes.GETSTATIC;

import java.math.BigInteger;
import lambda.rodeo.lang.compilation.CollectsErrors;
import lambda.rodeo.lang.scope.TypedModuleScope;
import lambda.rodeo.runtime.types.LRInteger;
import lombok.EqualsAndHashCode;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

@EqualsAndHashCode
public class IntType implements LambdaRodeoType, CompileableType {

  public static final IntType INSTANCE = new IntType();

  private IntType() {
  }

  @Override
  public String toString() {
    return "Int";
  }

  @Override
  public String getDescriptor() {
    return Type.getDescriptor(BigInteger.class);
  }

  @Override
  public String getInternalName() {
    return Type.getInternalName(BigInteger.class);
  }

  @Override
  public void provideRuntimeType(MethodVisitor methodVisitor) {
    methodVisitor.visitFieldInsn(GETSTATIC,
        Type.getInternalName(LRInteger.class),
        "INSTANCE",
        Type.getDescriptor(LRInteger.class));
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

  @Override
  public boolean isLambda() {
    return false;
  }
}
