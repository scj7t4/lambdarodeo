package lambda.rodeo.lang.statements;

import lambda.rodeo.lang.types.Type;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TypedValue {
  private final Object value;
  private final Type type;
}
