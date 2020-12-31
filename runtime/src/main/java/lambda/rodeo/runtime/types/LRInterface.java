package lambda.rodeo.runtime.types;

import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LRInterface implements LRType {
  private final Map<String, LRType> typeMap;

  public boolean fulfillsInterface(LRObject toTest) {
    for(String key : typeMap.keySet()) {
      LRType type = toTest.getType(key);
      LRType thisType = typeMap.get(key);
      if (!thisType.assignableFrom(type)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public boolean assignableFrom(LRType toTest) {
    if (!(toTest instanceof LRInterface)) {
      return false;
    }
    LRInterface asInterface = (LRInterface) toTest;

    for(String key : typeMap.keySet()) {
      LRType type = asInterface.getType(key);
      LRType thisType = typeMap.get(key);
      if (!thisType.assignableFrom(type)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public boolean isObjectOfType(Object object) {
    return object instanceof LRObject && this.fulfillsInterface((LRObject) object);
  }

  public LRType getType(String key) {
    return typeMap.getOrDefault(key, Atom.UNDEFINED);
  }
}
