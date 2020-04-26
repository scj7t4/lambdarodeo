package lambda.rodeo.lang.functions;

import lambda.rodeo.lang.types.Type;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TypedVarAst {

  private final String name;
  private final Type type;
}
