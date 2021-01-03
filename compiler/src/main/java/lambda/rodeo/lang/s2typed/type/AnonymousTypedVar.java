package lambda.rodeo.lang.s2typed.type;

import java.util.Objects;
import lambda.rodeo.lang.types.CompileableType;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AnonymousTypedVar implements S2TypedVar {
  private final String name;
  private final CompileableType type;

  public boolean equals(Object other) {
    return S2TypedVar.s2TypedVarEquals(this, other);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, type);
  }
}
