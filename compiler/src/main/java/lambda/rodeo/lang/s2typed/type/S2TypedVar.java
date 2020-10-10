package lambda.rodeo.lang.s2typed.type;

import lambda.rodeo.lang.s1ast.type.TypedVar;
import lambda.rodeo.lang.types.CompileableType;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
public class S2TypedVar {
  @NonNull
  private final TypedVar from;

  private final CompileableType type;

  public String getName() {
    return getFrom().getName();
  }

  public int getStartLine() {
    return getFrom().getStartLine();
  }

  public int getEndLine() {
    return getFrom().getEndLine();
  }

  public int getCharacterStart() {
    return getFrom().getCharacterStart();
  }
}
