package lambda.rodeo.lang.types;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.Builder;
import lombok.Getter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

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

  public void compile(MethodVisitor methodVisitor, Label start, Label end) {
    for (Entry scopeEntry : scope) {
      if (scopeEntry.getIndex() == -1) {
        continue;
      }

      /*
      methodVisitor.visitLocalVariable(
          scopeEntry.getName(),
          org.objectweb.asm.Type.getDescriptor(scopeEntry.getType().javaType()),
          null,
          start,
          end,
          scopeEntry.getIndex());
ZZ
       */
    }
  }

  @Builder
  @Getter
  public static class Entry {

    private final String name;
    private final Type type;
    private final int index;
  }
}
