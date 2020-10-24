package lambda.rodeo.lang.s2typed.type;

import lambda.rodeo.lang.s1ast.type.TypedVar;
import lambda.rodeo.lang.types.CompileableType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class SourcedTypedVar implements S2TypedVar {
  private final CompileableType type;
  private final TypedVar from;

  @Override
  public String getName() {
    return from.getName();
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
