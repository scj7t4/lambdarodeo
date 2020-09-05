package lambda.rodeo.lang.scope;

import java.util.stream.Stream;
import lambda.rodeo.runtime.types.CompileableType;
import lambda.rodeo.runtime.types.LambdaRodeoType;
import lombok.Builder;
import lombok.Getter;

public interface TypeScope {

  TypeScope EMPTY = new TypeScopeImpl();

  TypeScope declare(String varName, CompileableType type);

  Stream<Entry> get(String varName);

  Stream<Entry> getAll();

  CompileableTypeScope toCompileableTypeScope();

  int maxSlot();

  TypeScope parent();

  @Builder
  @Getter
  class Entry {

    private final String name;
    private final CompileableType type;
    private final int index;

    public CompileableTypeScope.Entry toCompileableEntry() {
      return CompileableTypeScope.Entry.builder()
          .index(index)
          .name(name)
          .type(type)
          .build();
    }
  }
}
