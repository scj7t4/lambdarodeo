package lambda.rodeo.lang.statements;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Scope {

  public static final Scope EMPTY = new Scope();

  private final Map<String, Object> scope;

  public Scope() {
    scope = new HashMap<>();
  }

  public Scope(Scope oldScope, String varName, Object value) {
    this.scope = new HashMap<>();
    this.scope.putAll(oldScope.scope);
    this.scope.put(varName, value);
  }

  public Scope put(String varName, Object value) {
    return new Scope(this, varName, value);
  }

  public Optional<Object> get(String varName) {
    return Optional.ofNullable(this.scope.get(varName));
  }
}
