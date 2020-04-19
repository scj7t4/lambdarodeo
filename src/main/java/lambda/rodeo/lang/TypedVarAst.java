package lambda.rodeo.lang;

import lambda.rodeo.lang.types.Type;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TypedVarAst {
  private final String name;
  private final Type type;
}