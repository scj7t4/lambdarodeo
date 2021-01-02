package lambda.rodeo.lang.scope;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lambda.rodeo.lang.types.CompileableType;

public class DerivedTypeScope implements TypeScope {

  private final Set<Integer> allowedEntries;
  private final TypeScope parent;

  public DerivedTypeScope(TypeScope parent, Set<Integer> allowedEntries) {
    this.parent = parent;
    this.allowedEntries = allowedEntries;
  }

  public DerivedTypeScope(TypeScope parent) {
    this(parent, parent);
  }

  public DerivedTypeScope(TypeScope parent, TypeScope initialTypeScope) {
    this(parent,
        initialTypeScope.getAllSimple()
            .map(SimpleEntry::getIndex)
            .collect(Collectors.toSet()));
  }

  @Override
  public DerivedTypeScope declare(String varName, CompileableType type) {
    TypeScope declare = parent.declare(varName, type);
    Set<Integer> newAllowed = new HashSet<>(allowedEntries);
    if (type.allocateSlot()) {
      newAllowed.add(declare.maxSlot());
    }
    return new DerivedTypeScope(declare, newAllowed);
  }

  public DerivedTypeScope replace(String varName, CompileableType type, int toReplace) {
    TypeScope declare = parent.declare(varName, type);
    Set<Integer> newAllowed = new HashSet<>(allowedEntries);
    newAllowed.remove(toReplace);
    if (type.allocateSlot()) {
      newAllowed.add(declare.maxSlot());
    }
    return new DerivedTypeScope(declare, newAllowed);
  }

  @Override
  public Stream<SimpleEntry> getSimple(String varName) {
    return parent.getSimple(varName)
        .filter(entry -> allowedEntries.contains(entry.getIndex()))
        .map(entry -> entry);
  }

  public Stream<SimpleEntry> getByIndex(int index) {
    return parent.getByIndex(index)
        .filter(entry -> Objects.equals(entry.getIndex(), index));
  }

  @Override
  public Stream<SimpleEntry> getAllSimple() {
    return this.parent
        .getAllSimple()
        .filter(x -> allowedEntries.contains(x.getIndex()));
  }

  @Override
  public CompileableTypeScope toCompileableTypeScope() {
    return CompileableTypeScope.builder()
        .scope(getAllSimple()
            .map(SimpleEntry::toCompileableEntry)
            .collect(Collectors.toList()))
        .build();
  }

  @Override
  public int maxSlot() {
    return parent.maxSlot();
  }

  @Override
  public TypeScope parent() {
    return parent.parent();
  }
}
