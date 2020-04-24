package lambda.rodeo.lang.types;

import java.math.BigInteger;

public class IntType implements Type {

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
}
