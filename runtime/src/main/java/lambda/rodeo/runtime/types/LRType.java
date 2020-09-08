package lambda.rodeo.runtime.types;

public interface LRType {

  boolean assignableFrom(LRType type);
}
