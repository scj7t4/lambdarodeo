package lambda.rodeo.lang.s2typed.type;

import lambda.rodeo.lang.types.CompileableType;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AnonymousTypedVar implements S2TypedVar {
  private final String name;
  private final CompileableType type;
}
