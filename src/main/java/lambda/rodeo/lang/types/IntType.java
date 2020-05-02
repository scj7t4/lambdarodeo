package lambda.rodeo.lang.types;

import java.math.BigInteger;
import lambda.rodeo.lang.typed.TypedModule;
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
  public CompileableType toCompileableType(TypedModule typedModule) {
    return this;
  }

  @Override
  public Type getType() {
    return this;
  }
}
