package lambda.rodeo.lang.scope;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import lambda.rodeo.lang.scope.CompileableTypeScope.CompileableEntry;
import lambda.rodeo.lang.types.CompileableType;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class TypeScopeImpl implements TypeScope {

  private final List<SimpleEntry> scope;

  public TypeScopeImpl() {
    scope = new ArrayList<>();
  }

  @Override
  public TypeScopeImpl declare(String varName, CompileableType type) {
    TypeScopeImpl out = new TypeScopeImpl();
    out.scope.addAll(this.scope);
    int slot = -1;
    if (type.allocateSlot()) {
      slot = maxSlot() + 1;
    }
    out.scope.add(SimpleEntry.builder()
        .name(varName)
        .type(type)
        .index(slot)
        .build());
    return out;
  }

  @Override
  public Stream<SimpleEntry> getSimple(String varName) {
    return this.scope.stream()
        .filter(entry -> Objects.equals(entry.getName(), varName));
  }

  @Override
  public Stream<SimpleEntry> getByIndex(int index) {
    return this.scope.stream()
        .filter(entry -> Objects.equals(entry.getIndex(), index));
  }

  @Override
  public Stream<SimpleEntry> getAllSimple() {
    return this.scope.stream();
  }

  @Override
  public CompileableTypeScope toCompileableTypeScope() {
    List<CompileableEntry> entries = new ArrayList<>();

    for (SimpleEntry entry : scope) {
      CompileableEntry converted = entry.toCompileableEntry();
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
        .map(SimpleEntry::getIndex)
        .filter(index -> index != -1)
        .mapToInt(x -> x)
        .max()
        .orElse(-1);
  }

  @Override
  public TypeScope parent() {
    return this;
  }
}
