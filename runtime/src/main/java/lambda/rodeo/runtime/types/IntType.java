package lambda.rodeo.runtime.types;

import java.math.BigInteger;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class IntType implements Type, CompileableType {

  public static final IntType INSTANCE = new IntType();

  private IntType() {
  }

  @Override
  public String toString() {
    return "int";
  }

  @Override
  public Class<?> javaType() {
    return BigInteger.class;
  }

  @Override
  public CompileableType toCompileableType() {
    return this;
  }

  @Override
  public Type getType() {
    return this;
  }
}
