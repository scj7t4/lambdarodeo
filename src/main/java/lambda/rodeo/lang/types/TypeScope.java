package lambda.rodeo.lang.types;

import static lambda.rodeo.lang.ast.ModuleAst.THIS_MODULE;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lambda.rodeo.lang.ast.ModuleAst;
import lambda.rodeo.lang.exceptions.CriticalLanguageException;
import lambda.rodeo.lang.typed.TypedModule;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

@Getter
@EqualsAndHashCode
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

    TypedModule thisModule = scope.stream()
        .filter(entry -> Objects.equals(entry.getName(), THIS_MODULE))
        .findFirst()
        .filter(entry -> entry.getType() instanceof ModuleType)
        .map(entry -> (ModuleType) entry.getType())
        .map(ModuleType::getModuleAst)
        .map(ast -> findModuleByAst(modules, ast))
        .orElseThrow(() -> new CriticalLanguageException("$this module is not defined"));

    for (Entry entry : scope) {
      Type type = entry.getType();
      TypedModule targetModule = null;
      if (type instanceof ModuleType) {
        @NonNull ModuleAst moduleAst = ((ModuleType) type).getModuleAst();
        targetModule = findModuleByAst(modules, moduleAst);
        if (targetModule == null) {
          throw new CriticalLanguageException("Cannot find typed version of module '"
              + moduleAst.getName() + "'");
        }
      }
      if (targetModule == null) {
        targetModule = thisModule;
      }

      CompileableTypeScope.Entry converted = CompileableTypeScope.Entry.builder()
          .index(entry.index)
          .name(entry.name)
          .type(entry.type.toCompileableType(targetModule))
          .build();
      entries.add(converted);
    }

    return CompileableTypeScope
        .builder()
        .scope(entries)
        .build();
  }

  private TypedModule findModuleByAst(List<TypedModule> modules, @NonNull ModuleAst moduleAst) {
    return modules.stream()
        .filter(x -> Objects.equals(moduleAst, x.getModuleAst()))
        .findFirst()
        .orElse(null);
  }

  @Builder
  @Getter
  public static class Entry {

    private final String name;
    private final Type type;
    private final int index;
  }
}
