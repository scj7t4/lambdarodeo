package lambda.rodeo.runtime.types;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

@EqualsAndHashCode
public final class LRObject {

  @Builder
  @Getter
  @EqualsAndHashCode
  static final class LRObjectEntry implements Comparable<LRObjectEntry> {

    @NonNull
    private final String key;
    @NonNull
    private final Object value;
    @NonNull
    private final LRType type;


    @Override
    public int compareTo(LRObjectEntry other) {
      return key.compareTo(other.getKey());
    }
  }

  private static final LRObject EMPTY = new LRObject(
      new LRObjectEntry[]{}
  );

  private final LRObjectEntry[] entries;

  public static LRObject create() {
    return EMPTY;
  }

  LRObject(@NonNull LRObjectEntry[] entries) {
    this.entries = Arrays.copyOf(entries, entries.length);
    Arrays.sort(this.entries);
  }

  public LRObjectSetter set(String key, Object value, LRType type) {
    return new LRObjectSetter(this, key, value, type);
  }

  public Optional<LRObjectEntry> getEntry(String key) {
    int left = 0;
    int right = entries.length - 1;
    int m;
    while (left <= right) {
      m = ((left + right) / 2);
      LRObjectEntry entryAtM = entries[m];
      int compared = entryAtM.getKey().compareTo(key);
      if (compared < 0) {
        left = m + 1;
      } else if (compared > 0) {
        right = m - 1;
      } else {
        return Optional.of(entryAtM);
      }
    }
    return Optional.empty();
  }

  public Object get(String key) {
    return getEntry(key)
        .map(LRObjectEntry::getValue)
        .orElse(Atom.UNDEFINED);
  }

  public LRType getType(String key) {
    return getEntry(key)
        .map(LRObjectEntry::getType)
        .orElse(Atom.UNDEFINED);
  }

  public LRObjectEntry[] getEntries() {
    return this.entries;
  }

  public Set<String> getKeys() {
    return Arrays.stream(entries)
        .map(LRObjectEntry::getKey)
        .collect(Collectors.toSet());
  }
}
