package lambda.rodeo.lang.types;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lambda.rodeo.lang.typed.TypedModule;
import lombok.Builder;
import lombok.Getter;

@Getter
public class TypeScope {

  public static final TypeScope EMPTY = new TypeScope();
  private final List<Entry> scope;

  public TypeScope() {
    scope = new ArrayList<>();
  }

  public TypeScope declare(String varName, Type type) {
    TypeScope out = new TypeScope();
    out.scope.addAll(this.scope);
    int slot = -1;
    if (type.allocateSlot()) {
      slot = out.scope.stream()
          .filter(entry -> entry.getIndex() != -1)
          .map(Entry::getIndex)
          .mapToInt(x -> x)
          .max()
          .orElse(-1) + 1;
    }
    out.scope.add(Entry.builder()
        .name(varName)
        .type(type)
        .index(slot)
        .build());
    return out;
  }

  public Optional<Entry> get(String varName) {
    return this.scope.stream()
        .filter(entry -> Objects.equals(entry.name, varName))
        .findFirst();
  }

  public CompileableTypeScope toCompileableTypeScope(List<TypedModule> modules) {
    List<CompileableTypeScope.Entry> entries = new ArrayList<>();

    for (Entry entry : scope) {
      CompileableTypeScope.Entry converted = CompileableTypeScope.Entry.builder()
          .index(entry.index)
          .name(entry.name)
          // TODO: get(0) is wrong
          .type(entry.type.toCompileableType(modules.get(0)))
          .build();
      entries.add(converted);
    }

    return CompileableTypeScope
        .builder()
        .scope(entries)
        .build();
  }

  @Builder
  @Getter
  public static class Entry {

    private final String name;
    private final Type type;
    private final int index;
  }
}
