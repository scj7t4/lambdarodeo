package lambda.rodeo.runtime.types;

public interface LRObjectSetter {

  public LRObjectSetter set(String key, Object value, LRType type);

  public LRObject done();
}
