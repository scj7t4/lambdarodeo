package lambda.rodeo.lang.s2typed.types;

import lambda.rodeo.lang.s1ast.type.TypedVar;
import lambda.rodeo.lang.types.CompileableType;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
public class S2SourcedTypedVar implements S2TypedVar {
  @NonNull
  private final TypedVar from;

  private final CompileableType type;

  @Override
  public String getName() {
    return getFrom().getName();
  }

  @Override
  public int getStartLine() {
    return getFrom().getStartLine();
  }

  @Override
  public int getEndLine() {
    return getFrom().getEndLine();
  }

  @Override
  public int getCharacterStart() {
    return getFrom().getCharacterStart();
  }
}
