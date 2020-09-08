package lambda.rodeo.runtime.types;

public class LRInteger implements LRType {

  public static final LRInteger INSTANCE = new LRInteger();

  private LRInteger() {}

  @Override
  public boolean assignableFrom(LRType type) {
    return type == INSTANCE;
  }
}
