package lambda.rodeo.lang.scope;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import lambda.rodeo.runtime.types.Type;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class TypeScopeImpl implements TypeScope {

  private final List<Entry> scope;

  public TypeScopeImpl() {
    scope = new ArrayList<>();
  }

  @Override
  public TypeScopeImpl declare(String varName, Type type) {
    TypeScopeImpl out = new TypeScopeImpl();
    out.scope.addAll(this.scope);
    int slot = -1;
    if (type.allocateSlot()) {
      slot = maxSlot() + 1;
    }
    out.scope.add(Entry.builder()
        .name(varName)
        .type(type)
        .index(slot)
        .build());
    return out;
  }

  @Override
  public Stream<Entry> get(String varName) {
    return this.scope.stream()
        .filter(entry -> Objects.equals(entry.getName(), varName));
  }

  @Override
  public Stream<Entry> getAll() {
    return this.scope.stream();
  }

  @Override
  public CompileableTypeScope toCompileableTypeScope() {
    List<CompileableTypeScope.Entry> entries = new ArrayList<>();

    for (Entry entry : scope) {
      CompileableTypeScope.Entry converted = entry.toCompileableEntry();
      entries.add(converted);
    }

    return CompileableTypeScope
        .builder()
        .scope(entries)
        .build();
  }

  @Override
  public int maxSlot() {
    return scope.stream()
        .filter(entry -> entry.getIndex() != -1)
        .map(Entry::getIndex)
        .mapToInt(x -> x)
        .max()
        .orElse(-1);
  }

  @Override
  public TypeScope parent() {
    return this;
  }
}
