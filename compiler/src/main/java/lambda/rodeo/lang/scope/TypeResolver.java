package lambda.rodeo.lang.scope;

import java.util.Optional;
import lambda.rodeo.lang.s1ast.type.TypeDef;

public interface TypeResolver {

  Optional<TypeDef> getTypeTarget(String typeTarget);
}
