package lambda.rodeo.lang.ast.functions;

import lambda.rodeo.lang.types.Type;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode
public class TypedVar {

  private final String name;
  private final Type type;
}
