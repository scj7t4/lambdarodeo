package lambda.rodeo.runtime.types;

import java.math.BigInteger;

public class LRInteger implements LRType {

  public static final LRInteger INSTANCE = new LRInteger();

  private LRInteger() {}

  @Override
  public boolean assignableFrom(LRType type) {
    return type == INSTANCE;
  }

  @Override
  public boolean isObjectOfType(Object object) {
    return object instanceof BigInteger;
  }
}
