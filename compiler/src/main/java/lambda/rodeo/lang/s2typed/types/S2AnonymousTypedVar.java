package lambda.rodeo.lang.s2typed.types;

import lambda.rodeo.lang.types.CompileableType;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class S2AnonymousTypedVar implements S2TypedVar {
  private final String name;
  private final CompileableType type;
  private final int startLine;
  private final int endLine;
  private final int characterStart;
}
