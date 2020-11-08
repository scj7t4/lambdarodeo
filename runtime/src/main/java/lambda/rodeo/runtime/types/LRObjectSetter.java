package lambda.rodeo.runtime.types;

public interface LRObjectSetter {

  LRObjectSetter set(String key, Object value, LRType type);

  LRObject done();
}
