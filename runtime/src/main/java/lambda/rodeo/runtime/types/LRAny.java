package lambda.rodeo.runtime.types;

public class LRAny implements LRType {

  public static final LRAny INSTANCE = new LRAny();

  private LRAny() {
  }

  @Override
  public boolean assignableFrom(LRType type) {
    return true;
  }

  @Override
  public boolean isObjectOfType(Object object) {
    return true;
  }
}
