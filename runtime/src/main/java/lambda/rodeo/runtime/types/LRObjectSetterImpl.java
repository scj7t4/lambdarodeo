package lambda.rodeo.runtime.types;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import lambda.rodeo.runtime.exceptions.RuntimeCriticalLanguageException;
import lambda.rodeo.runtime.types.LRObject.LRObjectEntry;
import lombok.NonNull;

public final class LRObjectSetterImpl implements LRObjectSetter {

  private final LRObject setting;
  private final LRObjectSetterImpl previous;
  private final String key;
  private final Object value;
  private final LRType type;

  public LRObjectSetterImpl(@NonNull LRObject setting,
      @NonNull String key,
      @NonNull Object value,
      @NonNull LRType type) {
    this.setting = setting;
    this.previous = null;
    this.key = key;
    this.value = value;
    this.type = type;
  }

  private LRObjectSetterImpl(@NonNull LRObjectSetterImpl previous,
      @NonNull LRObject setting,
      @NonNull String key,
      @NonNull Object value,
      @NonNull LRType type) {
    this.previous = previous;
    this.key = key;
    this.value = value;
    this.type = type;
    this.setting = setting;
  }

  @Override
  public LRObjectSetter set(String key, Object value, LRType type) {
    return new LRObjectSetterImpl(this, setting, key, value, type);
  }

  @Override
  public LRObject done() {
    Map<String, LRObjectEntry> packer = new HashMap<>();
    pack(packer);
    return new LRObject(packer.values().toArray(new LRObjectEntry[0]));
  }

  private LRObject getSetting() {
    if (this.setting != null) {
      return this.setting;
    }
    throw new RuntimeCriticalLanguageException("LRObjectSetter used without object being set?");
  }

  private void pack(Map<String, LRObjectEntry> pack) {
    if (previous != null) {
      previous.pack(pack);
    } else {
      Arrays.stream(this.setting.getEntries())
          .forEach(entry -> pack.put(entry.getKey(), entry));
    }
    pack.put(this.key, LRObjectEntry.builder()
        .key(this.key)
        .type(this.type)
        .value(this.value)
        .build());
  }
}
