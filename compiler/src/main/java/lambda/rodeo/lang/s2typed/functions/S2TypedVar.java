package lambda.rodeo.lang.s2typed.functions;

import lambda.rodeo.lang.s1ast.functions.TypedVar;
import lambda.rodeo.runtime.types.CompileableType;
import lambda.rodeo.runtime.types.LambdaRodeoType;
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