package lambda.rodeo.lang.types;

import java.math.BigInteger;
import lombok.EqualsAndHashCode;
import org.objectweb.asm.Type;

@EqualsAndHashCode
public class IntType implements LambdaRodeoType, CompileableType {

  public static final IntType INSTANCE = new IntType();

  private IntType() {
  }

  @Override
  public String toString() {
    return "int";
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
  public CompileableType toCompileableType() {
    return this;
  }

  @Override
  public LambdaRodeoType getType() {
    return this;
  }
}
