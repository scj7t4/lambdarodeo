package lambda.rodeo.lang.s1ast.functions;

import lambda.rodeo.runtime.types.Type;
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
