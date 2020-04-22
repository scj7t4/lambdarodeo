package lambda.rodeo.lang.statements;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lambda.rodeo.lang.types.Type;

public class TypeScope {
  public static final TypeScope EMPTY = new TypeScope();

  private final Map<String, Type> scope;

  public TypeScope() {
    scope = new HashMap<>();
  }

  public TypeScope(TypeScope oldScope, String varName, Type type) {
    this.scope = new HashMap<>();
    this.scope.putAll(oldScope.scope);
    this.scope.put(varName, type);
  }

  public TypeScope put(String varName, Type type) {
    return new TypeScope(this, varName, type);
  }

  public Optional<Type> get(String varName) {
    return Optional.ofNullable(this.scope.get(varName));
  }
}
