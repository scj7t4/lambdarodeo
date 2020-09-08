package lambda.rodeo.runtime.types;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LRObject {

  private final Map<String, Object> value;
  private final Map<String, LRType> type;

  public LRObject(Map<String, Object> value, Map<String, LRType> type) {
    this.value = value;
    this.type = type;
  }

  public LRObject set(String key, Object value, LRType type) {
    HashMap<String, Object> outValue = new HashMap<>(this.value);
    HashMap<String, LRType> outType = new HashMap<>(this.type);
    outValue.put(key, value);
    outType.put(key, type);
    return new LRObject(outValue, outType);
  }

  public Object get(String key) {
    return value.getOrDefault(key, Atom.UNDEFINED);
  }

  public LRType getType(String key) {
    return type.getOrDefault(key, Atom.UNDEFINED);
  }

  public Set<String> keys() {
    return value.keySet();
  }
}
