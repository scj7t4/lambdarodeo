package lambda.rodeo.lang.scope;

import java.util.Map;
import java.util.Optional;
import lambda.rodeo.lang.s1ast.type.TypeDef;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BoundGenericsTypeResolver implements TypeResolver {
  private final TypeResolver parent;
  private final Map<String, TypeDef> overrides;

  @Override
  public Optional<TypeDef> getTypeTarget(String typeTarget) {
    if (overrides.containsKey(typeTarget)) {
      return Optional.of(overrides.get(typeTarget));
    }
    return parent.getTypeTarget(typeTarget);
  }
}
