package lambda.rodeo.lang.statements;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lambda.rodeo.lang.types.Atom;
import lambda.rodeo.lang.types.Type;
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
    out.scope.add(Entry.builder()
        .name(varName)
        .type(type)
        .index(out.scope.size())
        .build());
    return out;
  }

  public Optional<Entry> get(String varName) {
    return this.scope.stream()
        .filter(entry-> Objects.equals(entry.name, varName))
        .findFirst();
  }

  public void compile(MethodVisitor methodVisitor, Label start, Label end) {
    for(Entry scopeEntry : scope) {
      methodVisitor.visitLocalVariable(
          scopeEntry.getName(),
          org.objectweb.asm.Type.getDescriptor(scopeEntry.getType().javaType()),
          null,
          start,
          end,
          scopeEntry.getIndex());
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
