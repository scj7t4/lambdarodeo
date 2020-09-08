package lambda.rodeo.runtime.types;

public class LRString implements LRType {

  public static final LRString INSTANCE = new LRString();

  private LRString() {
  }

  @Override
  public boolean assignableFrom(LRType type) {
    return type == INSTANCE;
  }
}
